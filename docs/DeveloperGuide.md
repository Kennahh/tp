# Developer Guide

- [Acknowledgements](#acknowledgements)
- [Setting up, getting started](#setting-up-getting-started)
- [Design](#design)
  - [Architecture](#architecture)
  - [Parser](#parser-and-commands-component)
  - [Commands](#parser-and-commands-component)
  - [Activity](#activity-and-gpa)
  - [Gpa](#activity-and-gpa)
  - [Storage](#storage)
  - [Ui](#ui)
  - [Exceptions](#exceptions)
- [Implementation](#implementation)
  - [Activities](#activities)
    - [Add Task with priority](#add-task-with-priority-and-robust-datetime-parsing)
    - [Task Deadline & Priority System](#task-deadline--priority-system)
    - [Change priority rebalancing](#change-priority-rebalancing)
    - [Check current tasks](#check-current-tasks-earliest-deadlines)
  - [Gpa Tracker](#gpa-tracker)
  - [Parsing helpers](#parsing-helpers)
    - [Day-of-week parsing](#day-of-week-parsing)
- [Appendix A: Product Scope](#appendix-a-product-scope-expanded)
- [Appendix B: User Stories](#appendix-b-user-stories-additional)
- [Appendix C: Non-Functional Requirements](#appendix-c-non-functional-requirements)
- [Appendix D: Glossary](#appendix-d-glossary-expanded)
- [Appendix E: Instructions for Manual Testing](#appendix-e-instructions-for-manual-testing-full)

---

## Acknowledgements

This Developer Guide follows the general structure and documentation practices used by SE‑EDU projects and other public references. We use PlantUML for diagrams. Below we cite all sources of ideas/code/documentation we reused or adapted, and the extent of reuse.

- SE‑EDU AddressBook Level 3 (AB3)
  - Documentation structure and examples were referenced from AB3’s Developer Guide and User Guide.
  - Links: AB3 DG
  - Extent: Documentation structure and some phrasing inspired; no production code copied unless explicitly stated below.
- CS2113 Main Website
  - Used as reference for diagram drawing, and expectations of what there is to be in DeveloperGuide.
  - Extent: Concepts and notation guidance only.
- SE‑EDU Guides — PlantUML Tutorial
  - Used as reference for authoring and styling diagrams. Link: Using PlantUML @ SE‑EDU/guides.
  - Extent: Concepts and notation guidance only.

## Setting up, getting started

Refer to the project README for setup instructions: [README.md](../README.md)

## Design

### 💡 Tips

The `.puml` files used to create diagrams are in this document `docs/diagrams` folder.

### Architecture

This section gives a high-level overview of Astra’s components and how they interact in a typical command flow. (Astra → Parser → Command → Model/Storage → Ui).

**Key components and responsibilities**

Astra (`astra.astra`): Main program in charge of the app launch and shut down.

- At app launch, it initialises the other components and connects them up.
- During program run, it scans user inputs and maintains the running of the Astra program.
- At shut down, it shuts down the other components and invokes all necessary cleanup.

Astra is built upon these main components:
- [Parser](#parser-and-commands-component) (`astra.parser.Parser`): Maps raw input to a concrete command.
- [Commands](#parser-and-commands-component) (`astra.command.*`): Execute logic using collaborators.
- [Activity](#activity-and-gpa)/[Gpa](#gpa-model-details) (`astra.activity.*`, `astra.gpa.*`): In‑memory data for activities and GPA.
- [Storage](#storage) (`astra.data.Notebook`): Centralized persistence for activities and GPA.
- [Ui](#ui) (`astra.ui.Ui`): Console I/O and help printing.
- [Exceptions](#exceptions) (`astra.exception.*`): Exceptions shared across components.

![Architecture_Diagram](images/architecture_component.png)

**Main loop overview**

The main loop below shows how Astra orchestrates parsing, command execution, and persistence each cycle.

- Inputs: a single line of user text (REPL), or a parsed token stream.
- Outputs: a textual response to the user (via Ui) and, when needed, updated persisted state (via Notebook).
- Error modes: parsing errors, command validation errors, IO/persistence failures.
- Success criteria: correct command semantics, consistent persisted state, clear error messages, and no index corruption for list operations.

![Main loop sequence](images/main_loop_sequence.png)

**How the architecture components interact**

The sequence diagram below shows how components interact when the user issues `delete 1` (or multiple indices like `delete 2`).

- Commands share a `Command` interface and are executed directly.
- `Parser`, `Ui`, and `Notebook` are concrete classes used directly.
- Dependencies are passed explicitly into `Command.execute(ActivityList, Ui, Notebook)`, keeping flows easy to trace and limiting global coupling.

![Delete flow sequence](images/architecture_sequence_delete.png)

**Key points**

- `Parser.parse(input)` returns a concrete `Command`.
- `Command.execute(...)` returns `shouldExit`.
- On non-exiting commands, Astra persists activities using both `writeToFile(activities.toList())` and `saveToFile(activities)`.
- Some commands also call save methods (e.g., delete), which can lead to duplicate writes; see [Storage](#storage) for notes.

**Why this architecture (benefits)**
Separation of concerns (SoC)

### Parser and Commands component

API: `astra.parser.Parser`, `astra.command.Command`

How parsing works

- The top-level `Parser` performs command-word routing by inspecting the first token(s) (e.g., add, list, delete, gpa).
- Detailed argument parsing and validation is handled inside each concrete `Command` during construction and/or `execute`.
- Helpers like `DateTimeParser` and `dayOfWeekParser` are used to keep date/day parsing consistent across commands.

Command component then executes the necessary actions and amend data stored in other classes (see diagram below)

How it fits together

- `Parser.parse(input)` identifies the command and returns a concrete `Command`.
- `Command.execute(ActivityList, Ui, Notebook)` validates, mutates ActivityList as needed, may persist via `Notebook`, and reports via `Ui`. It returns a boolean `shouldExit` to control the REPL.

![Command component](images/CommandComponent.png)

### Activity and GPA

API: `astra.activity.*`, `astra.gpa.*`

![Model component](images/model_component.png)

The Model (consisting of Activity and GPA) component

- Stores the app’s domain data in memory:
  - Activities: `ActivityList` containing `Activity` subtypes `Task`, `Lecture`, `Tutorial`, `Exam`.
  - GPA: `GpaList` of `GpaEntry` objects.
- Provides operations to create, access, and modify domain objects:
  - Activities: `addActivity`, `deleteActivity`, `getActivity`, `getListSize`, `toList`, and task-specific helpers
    such as `addTaskWithPriority` (rebalances priorities) and deletion that re-denses priorities.
  - GPA: `add`, `remove(1‑based)`, `toList` (unmodifiable), `computeGpa`, `clear`.
- Validation and invariants:
  - `GpaEntry` validates subject (single token), grade (must be in allowed set), and MC (non‑negative); S/U excluded from GPA.
  - Assertions in `GpaList` guard arithmetic and index handling.
- Independence and responsibilities:
  - Activity/GPA is largely independent of Ui/Parser. It does not perform persistence; `Notebook` (Storage) handles file I/O.
  - Note: `ActivityList` currently contains some printing utilities used by commands (e.g., reminders). These are harmless but can be refactored out to keep Activity purely non‑UI.

#### Activities subcomponent

Packages/classes: `astra.activity.*` — `Activity`, `SchoolActivity`, `Task`, `Lecture`, `Tutorial`, `Exam`, `ActivityList`.

![Activity component](images/Activity_component.png)

Key notes

- Priority management is encapsulated in the model (`addTaskWithPriority`, deletion rebalancing) so multiple commands remain consistent.
- Deadlines and day/time are represented using `java.time` types.

#### GPA subcomponent

Packages/classes: `astra.gpa.*` — `GpaEntry` (validation, grade mapping), `GpaList` (storage + `computeGpa`).

Key notes

- `GpaList.toList()` returns an unmodifiable view to prevent accidental mutation by callers.
- `computeGpa()` ignores S/U and invalid grade points; returns 0.0 when there are no counted MCs.

See [GPA Tracker](#gpa-tracker) for full implementation.

### Storage

API: `astra.data.Notebook`

- Single owner for file I/O: activities (pipe format) and GPA (`gpa.txt` + `gpa.csv`).
- On mutations, commands call save methods on `Notebook`.
  - Activities use `saveToFile(ActivityList)` (CSV with headers written each save).
  - GPA uses `saveGpa()` which writes both pipe (`gpa.txt`) and CSV (`gpa.csv`).
- Additionally, the main loop persists activities after each non-exiting command using `writeToFile(activities.toList())` (pipe format) and `saveToFile(activities)` (CSV). This ensures durability even if a command forgets to save, at the cost of potential duplicate writes when a command also saves. A future improvement is to standardize persistence in one place (either commands or the loop) to avoid duplication.

### UI

API: `astra.ui.Ui`

- Presents results, lists, errors, and help.
- Delegates parsing to `Parser` and triggers command execution.

### Exceptions

`astra.exception.*` — `InputException`, `GpaInputException`, `FileSystemException`; used to surface user-friendly errors and encapsulate file I/O failures.

---

## Implementation

### Activities

#### Add Task with priority and robust date/time parsing

Flow: The user enters `task <desc> /by <YYYY-MM-DD> <HH:MM> /priority <n>`. The parser returns an `AddCommand` (task variant), which validates, uses `DateTimeParser` to parse date/time, and inserts the task at the desired priority by bumping existing priorities that are ≥ the requested priority. The command persists via `Notebook.saveToFile(activities)`.

![Add task sequence](images/add_task_sequence.png)

Why

- Keeping parsing and validation in the command enables precise error messages and reuse of the generic `Parser`.
- `ActivityList.addTaskWithPriority` encapsulates the re‑prioritization logic, avoiding scattered priority updates across commands.

Alternatives considered

- Assigning priority based on current list order was rejected as it breaks when tasks are filtered/sorted differently for view.

---

### Task Deadline & Priority System

This section documents the deadline and priority system among task instances and how it interacts with the rest of the system (Ui, Parser, Commands, Notebook, and the Astra app). It also includes UML diagrams to aid future developers.

### Overview

The **Task Deadline and Priority System** extends the `activity` package for `Task` instances to enhance user productivity by enabling time and priority management for all task-type activities.
It introduces **two key features**:

1. **CheckCurrent** — allows users to display [n] amounts of Task based on earliest deadline, default n = 1.
2. **ChangeDeadlineCommand** — allows users to modify the deadline of existing tasks.
3. **CheckPriority** — allows users to display all Task in the order of priority (lowest to highest number)
4. **ChangePriorityCommand** — allows users to swap around the priority of existing tasks.
5. **Priority Management** — automatically manages task priorities upon creation, update, and deletion.

Together, these features ensure that users can efficiently monitor their task by order of each task's deadline or what they define to be most critical tasks.

---

#### Check current tasks (Earliest Deadlines)

**Purpose**: Show the nearest upcoming task deadlines (future tasks), optionally limited by a count. Useful to get a quick glance at what is due soon.

`checkcurrent [n]` lists the n closest upcoming tasks (default 1). It filters for deadlines after “now”, sorts by deadline ascending, and prints the first n.

![Check Current sequence](images/CheckCurrent_sequence.png)

#### Behaviour

- Filters `ActivityList` for `Task` instances whose deadline is after current time.
- Sorts matching tasks by combined `deadlineDate` + `deadlineTime` (earliest first).
- Returns up to `n` tasks where `n` is provided by the user; defaults to 1 when omitted or invalid.
- Uses `Ui` to display friendly messages when no future tasks exist.

---

#### Change Deadline Command

Code format — `changedeadline <taskNumber> /to <YYYY-MM-DD> <HH:MM>`. Finds the <taskNumber> activity in the activity list, checks if its a task before changing and validating the deadline based on DateTime format

Change Deadline of task 3 — `changedeadline 3 /to 2025-10-31 18:00`
![Change Deadline sequence](images/changeDeadline_sequence.png)

#### Validation checks and behaviours

- Validates `/to` presence and the index argument.
- Uses `DateTimeParser` to accept flexible date/time formats (YYYY-MM-DD, or day names where supported).
- Ensures selected activity is a `Task`; otherwise shows an error via `Ui`.
- Persists changes through `Notebook.writeToFile(...)` and `Notebook.saveToFile(...)` so the updated deadline survives restarts.

---

#### CheckPriorityCommand

Display all tasks ordered by their priority — `checkpriority`

![Check Priority sequence](images/CheckPriority_sequence.png)


#### Behaviour
- Filters `ActivityList` for all `Task` instances and plac into new `ActivityList` instance.
- Sorts matching tasks by priority through `task.getPriority`.
- Uses `Ui` to display friendly messages when no tasks exist.

---

#### Change priority rebalancing

Update and rebalance task priority — `changepriority <taskIndex> /to <newPriority>`

![Change priority activity](images/change_priority_activity.png)



---

Change priority of task 3 — `changepriority 3 /to 1`

![Change Priority sequence](images/changePriority_sequence.png)

#### Behavior Edge cases handled

- Moving a task down (e.g., 2 → 5): tasks in (2,5] shift up by 1.
- Moving a task up (e.g., 5 → 2): tasks in [2,5) shift down by 1.
- Non‑task indices or out‑of‑range priorities produce user‑friendly errors.

---

### GPA Tracker

This section documents the GPA Tracker as implemented in the codebase, and how it interacts with the rest of the
system (Ui, Parser, Commands, Notebook, and the Astra app). It also includes rationale, alternatives considered, and UML
diagrams to aid future developers.

### Overview

GPA Tracker stores module entries (subject, grade, MCs) and computes GPA on demand. It is integrated into the existing
command-based architecture.

Key components and responsibilities

- Parser and Commands
  - `astra.parser.Parser` recognizes GPA-related commands and instantiates the corresponding command classes.
  - Commands in `astra.command`: `AddGpaCommand`, `ListGpaCommand`, `DeleteGpaCommand`, `ComputeGpaCommand` implement
    the behaviour.
- GPA subcomponent
  - `astra.gpa.GpaEntry`: immutable value object for a single module entry; validates inputs and maps letter grades to
    points.
  - `astra.gpa.GpaList`: holds entries and computes GPA, excluding S/U entries.
- Persistence
  - `astra.data.Notebook`: owns a `GpaList`, loads GPA entries at startup, and persists them to `data/gpa.txt` and
    `data/gpa.csv`.
- UI and Application
  - `astra.ui.Ui`: prints feedback, errors, and help content including GPA usage.
  - `astra.Astra`: application entrypoint; runs the REPL loop, wires Parser → Command → Model/Notebook → Ui, and
    persists after each non-exiting command.

### Architecture context (class/component diagram)

![GPA component](images/GPA_Arch_Class_Diagram.png)

Notes

- `Notebook` owns `GpaList` and encapsulates persistence to both pipe format (`gpa.txt`) and CSV (`gpa.csv`).
- Commands operate via `Notebook` to modify/read GPA state and persist changes, keeping I/O in one place.

### Command flows (sequence diagrams)

Add GPA entry — `add gpa <SUBJECT> <GRADE> <MC>`

![Add GPA entry sequence](images/AddGPAEntry.png)

Compute GPA — `gpa`

![Compute GPA sequence](images/ComputeGPA.png)

List GPA entries — `list gpa`

![List GPA entries sequence](images/LIstGPAEntries.png)

Delete GPA entry — `delete gpa <INDEX>`

![Delete GPA entry sequence](images/DeleteGPAEntry.png)

### GPA model details

`GpaEntry`

- Invariants and validation: subject is a single non-blank token; grade is uppercased and validated; MC is non-negative.
- Grade mapping (to grade points): A+/A=5.0, A-=4.5, B+=4.0, B=3.5, B-=3.0, C+=2.5, C=2.0, D+=1.5, D=1.0, F=0.0; S/U are
  excluded.
- Persistence helpers: `toPipe()` and `toCsv()` provide stable line formats.

`GpaList`

- Core ops: `add`, `remove(1-based)`, `toList()` (unmodifiable view), `clear()`.
- GPA computation ignores entries where `isSu()` is true or `gradePoints()` yields `NaN`.
- Defensive assertions guard against invalid states during iteration and arithmetic.

### Persistence design

- Text file: `data/gpa.txt` uses a simple pipe format: `GPA | <SUBJECT> | <GRADE> | <MC>`.
- CSV file: `data/gpa.csv` is written alongside for interoperability (header: `Subject,Grade,MC`).
- Load: `Notebook` tries to read `gpa.txt` at startup; failures are surfaced as `FileSystemException` but do not crash
  the app.
- Save: Commands call `notebook.saveGpa()` after a mutation, which updates both files atomically per run.

Activity for save

![Activity for save](images/ActivityforSave.png)

### Why this design

- Single owner for persistence (Notebook) keeps I/O concerns centralized and reduces coupling in commands.
- Immutable `GpaEntry` objects simplify reasoning and avoid partial updates.
- Treating S/U as excluded (via `isSu` and `NaN` in `gradePoints`) keeps the computation logic simple while surfacing
  invalid grades early.

### Error handling and edge cases

- Parser validation: GPA commands are routed early by prefix checks in `Parser.parse(...)` to avoid ambiguity with
  general `add/list/delete`.
- Command input validation: friendly error messages via `Ui.showError(...)` for invalid grades, MC tokens, or indices.
- File I/O failures: wrapped as `FileSystemException`, caught in commands or main loop so the app remains usable.
- Empty GPA list: `computeGpa()` returns 0.0 and `list gpa` prints a helpful message.

### Parsing helpers

#### Day-of-week parsing

`Parser.dayOfWeekParser` accepts both numerals (1–7) and text (e.g., mon/Mon/Monday). Errors are reported early with actionable messages.

![Day-of-week parsing](images/parser_dayofweek.png)

Design note

- `DateTimeParser` centralizes date/time acceptance criteria while `Parser.dayOfWeekParser` handles day parsing; this separation simplifies testing and reduces coupling.

---

---

## Appendix A: Product Scope (expanded)

Primary users: students managing academic schedules (tasks, lectures, tutorials, exams) and monitoring GPA. Secondary: TAs/instructors demoing command‑driven planners.

Out‑of‑scope: authentication, networked sharing, and calendar sync (proposed future work).

## Appendix B: User Stories (additional)


| Version | As a ...| I want to ...                                        | So that I can ...                                       |
|---------|---------|------------------------------------------------------|---------------------------------------------------------|
| v1.0    | new user| see usage instructions                               | refer to them when I forget how to use the application  |
| v1.0    | Student | view all the tasks I need to do                      | see what is scheduled ahead                             |
| v1.0    | Student | Add tasks and their deadlines                        | track deadlines                                         |
| v1.0    | user    | Delete tasks                                         | reduce clutter in my list of tasks                      |
| v1.0    | Student | check what the current task is                       | know what mas be done right now                         |
| v1.0    | Student | Mark tasks as complete/incomplete                    | know if I have finished a task                          |
| v1.0    | Student | Change deadline of tasks                             | Change the deadline if i put it in wrongly              |
| v1.0    | Student | Check upcoming examinations                          | see my upcoming examinations                            |
| v1.0    | Student | Check what lectures I have                           | see what lecture I have for the day                     |
| v1.0    | Student | Check what tutorials I have                          | see what tutorials I have for the day                   |
| v1.0    | user    | See my added activities between startups             | have continuity between each start of the programme     |
| v2.0    | Student | List tasks by their deadline                         | see the order of tasks, by the earliest deadline first  |
| v2.0    | Student | List tasks by their priority                         | see the order of tasks, by the most important one first |
| v2.0    | Student | Have more ways to input dates and times              | have an easier time entering them in                    |
| v2.0    | Student | Be reminded of oncoming deadline                     | be reminded of the work to be done                      |
| v2.0    | user    | Delete multiple tasks                                | quickly delete and clear completed tasks                |
| v2.0    | user    | Have completed tasks or passed exams be auto-deleted | not have to manually clear them all                     |
| v2.0    | user    | track tasks and schedules                            | plan my week efficiently                                |
| v2.0    | student | track my modules and GPA                             | know my academic standing quickly                       |
| v2.0    | student | change a task’s priority quickly                     | focus on what matters first                             |
| v2.0    | student | see the next N deadlines                             | plan my immediate workload                              |
| v2.0    | student | list lectures/tutorials by day                       | plan my day efficiently                                 |
| v2.0    | student | edit a task deadline                                 | adapt when plans change                                 |

## Appendix C: Non-Functional Requirements

- Data durability: writes either fully succeed or fail with a clear error; partial writes avoided via overwrite semantics.
- Portability: no native dependencies; runs on JDK 17 across Windows/macOS/Linux.

## Appendix D: Glossary (expanded)

- Priority band — contiguous range 1..N used to rank tasks uniquely.
- Upcoming — a deadline strictly after the current wall‑clock time.

## Appendix E: Instructions for Manual Testing (full)

The following complements existing GPA tests and covers activity features. Copy‑paste the commands as shown.

1. Add tasks and priorities

- `task CS2113 Quiz /by 2025-11-01 23:59 /priority 1`
- `task CG2271 Lab /by 2025-10-30 20:00 /priority 1` (expect previously priority‑1 task to shift to 2)
- `list` (verify ordering/priority in output)

2. Change priority (rebalance)

- `changepriority 2 /to 1` (the previously index‑2 task should become priority 1; others shift accordingly)
- `list` (verify priorities are a dense 1..N)

3. Edit deadline

- `changedeadline 1 /to 2025-10-31 18:00` (updates date/time; verify in list)

4. Check current deadlines

- `checkcurrent` (shows the single nearest task)
- `checkcurrent 3` (shows up to three tasks, in ascending deadline order)

5. Filter by day and exam listing

- Add one lecture and one tutorial (use `lecture ...` and `tutorial ...` with day/time)
- `checklecture Friday`
- `checktutorial Wed`
- `checkexam` (after adding at least one exam)

6. Delete and multiple delete

- `delete 1` removes item #1
- Add a few more then `delete 2 4` removes multiple items; list to confirm

7. Error cases to verify guardrails

- `task X /priority 1` (missing `/by` → friendly error)
- `changedeadline 99 /to 2025-10-10 20:00` (out‑of‑range index)
- `changepriority 1 /to 0` (invalid priority)

8. GPA Tracker quick tests

- Add entries:
  - `add gpa CS2040C A+ 4mc`
  - `add gpa CFG1002 S 4`
  - Expected: success messages printed; files `data/gpa.txt` and `data/gpa.csv` updated
- List entries:
  - `list gpa` shows indexed list
- Compute GPA:
  - `gpa` prints `Current GPA: 5.00` for the first entry; with the S entry, it’s still 5.00 as S is excluded
- Delete entry:
  - `delete gpa 2` removes the second entry
- Invalid grade:
  - `add gpa CS1231X HH 4` shows an error
