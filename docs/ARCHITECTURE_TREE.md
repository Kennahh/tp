# ASTRA Source Architecture Tree

A text-only, skimmable map of the src layout: packages, classes, and their roles. For diagrams and deeper rationale, see `docs/DeveloperGuide.md`.

---

## Production code — `src/main/java`

- `astra/`
  - `Astra.java` — Application entry point. Runs REPL loop, wires Parser → Command → Ui/Notebook, persists after commands.
  - `activity/` (domain: activities)
    - `Activity.java` — Abstract base; holds `description`; `writeToFile()` contract.
    - `SchoolActivity.java` — Abstract base for venue/time-bound activities; adds `venue`, `startTime`, `endTime`.
    - `Task.java` — To-do with `deadlineDate`, `deadlineTime`, `isComplete`, `priority`; formatting + legacy serialization.
    - `Lecture.java` — Weekly session with `DayOfWeek`, start/end times.
    - `Tutorial.java` — Weekly session; similar to Lecture.
    - `Exam.java` — Dated assessment with `LocalDate` and start/end times.
    - `ActivityList.java` — In-memory list; add/delete/list, 3‑day reminders, overdue purge, priority management (insertion + compaction on delete).
  - `command/` (logic: commands)
    - `Command.java` — Interface; `execute(ActivityList, Ui, Notebook): boolean`.
    - `AddCommand.java` — Marker/base for add-like commands.
    - `AddTaskCommand.java` — `task <desc> /by <YYYY-MM-DD> <HH:MM> /priority <n>`; parses, inserts with priority rules; persists.
    - `AddLectureCommand.java` — `lecture <desc> /place <venue> /day <day> /from <HH:MM> /to <HH:MM>`; validates and adds.
    - `AddTutorialCommand.java` — `tutorial <desc> /place <venue> /day <day> /from <HH:MM> /to <HH:MM>`; validates and adds.
    - `AddExamCommand.java` — `exam <desc> /place <venue> /date <YYYY-MM-DD> /from <HH:MM> /to <HH:MM>`; validates and adds.
    - `DeleteCommand.java` — `delete <i1> <i2> ...>`; deletes multiple indices; adjusts task priorities; persists.
    - `ListCommand.java` — Lists all activities.
    - `ChangeDeadlineCommand.java` — `changedeadline <taskIndex> /to <YYYY-MM-DD> <HH:MM>`; updates a Task deadline.
    - `ChangePriorityCommand.java` — `changepriority <taskIndex> /to <newPriority>`; rebalances other tasks to keep a dense band.
    - `CompleteCommand.java` — `complete <index>`; mark Task complete.
    - `UnmarkCommand.java` — `unmark <index>`; mark Task incomplete.
    - `CheckCommand.java` — Marker/base for check-like commands.
    - `CheckExamCommand.java` — Lists only `Exam` activities.
    - `CheckLecturesCommand.java` — `checklecture <day>`; filters `Lecture` by day.
    - `CheckTutorialsCommand.java` — `checktutorial <day>`; filters `Tutorial` by day.
    - `CheckCurrentCommand.java` — `checkcurrent [n]`; N nearest upcoming Task deadlines.
    - `CheckPriorityCommand.java` — Lists Tasks sorted by ascending priority.
    - `HelpCommand.java` — Prints help usage.
    - `ExitCommand.java` — Exits application.
    - GPA commands:
      - `AddGpaCommand.java` — `add gpa <SUBJECT> <GRADE> <MC|#mc>`; validates; adds `GpaEntry`; calls `Notebook.saveGpa()`.
      - `ListGpaCommand.java` — Lists indexed GPA entries.
      - `DeleteGpaCommand.java` — `delete gpa <index>`; removes by 1‑based index; saves GPA.
      - `ComputeGpaCommand.java` — `gpa`; computes and prints current GPA via `GpaList`.
  - `data/` (persistence)
    - `Notebook.java` — Persistence owner for activities and GPA.
      - Activities (modern pipe format): `writeToFile(List<Activity>)`, `loadFromFile()`; internal `parseLine(...)`/`serializeActivity(...)`.
      - Activities (legacy comma format): `saveToFile(ActivityList)`, `loadFile()` for backward compatibility.
      - GPA: `getGpaList()`, `loadGpaFromFile()`, `writeGpaToFile()`, `writeGpaCsv()`, `saveGpa()`.
  - `exception/` (error types)
    - `InputException.java` — Invalid user input or parse errors.
    - `FileSystemException.java` — IO/format issues for storage.
    - `GpaInputException.java` — GPA validation errors.
  - `gpa/` (domain: GPA)
    - `GpaEntry.java` — Immutable record: `subject` (single token, uppercased), `grade` (validated), `mc` (≥0); `gradePoints()`, `isSu()`, `toPipe()`, `toCsv()`.
    - `GpaList.java` — Holds entries; `add`, `remove(1‑based)`, `toList`, `clear`, `size`, `computeGpa()` excluding S/U.
  - `parser/` (parsing and routing)
    - `Parser.java` — Routes raw input to commands; explicit GPA fast paths; `dayOfWeekParser(...)` helper.
    - `DateTimeParser.java` — Robust date/time parsing; multiple accepted formats; `today` shortcut.
  - `ui/` (presentation)
    - `Ui.java` — Console output; banners, prompts, showMessage/showError/showHelp.

---

## Test code — `src/test/java`

- `astra/activity/`
  - `ActivityListTest.java`, `TaskTest.java`, `LectureTest.java`, `TutorialTest.java`, `ExamTest.java` — Unit tests for activity model and list behaviour.
- `astra/command/`
  - `CreateCommandsTest.java` — Add commands.
  - `DeleteCommandTest.java` — Deleting activities.
  - `UpdateCommandsTest.java` — Deadline and priority updates.
  - `CheckCommandsTest.java` — Check family (exams, lectures/tutorials, current).
  - `GpaCommandsTest.java` — GPA add/list/delete/compute flows.
  - `ListHelpExitTest.java` — List/help/exit behaviour.
- `astra/data/`
  - `NotebookTest.java` — Persistence tests for activities/GPA.
- `astra/parser/`
  - `AstraParserTest.java`, `ParserBasicTest.java` — Routing and parsing basics.
  - `DateTimeParserTest.java` — Date/time parsing acceptance.
- `astra/ui/`
  - `UiTest.java` — Help/messages structure.
- `astra/testutil/`
  - `TestUi.java` — Test utility/stub for UI.

---

## Storage formats (summary)

- Activities (modern): `TYPE | done(0|1) | ...` pipe-separated lines. Supported types: `TASK`, `LECTURE`, `TUTORIAL`, `EXAM`.
- Activities (legacy): Comma-separated friendly lines via individual `writeToFile()` in activity classes.
- GPA (text): `GPA | SUBJECT | GRADE | MC` in `data/gpa.txt`.
- GPA (CSV): `Subject,Grade,MC` in `data/gpa.csv`.

## Control flow (summary)

Input → `Parser.parse(...)` → `Command` → `Command.execute(activities, ui, notebook)` → mutate model →
- Activities: `Astra` calls `Notebook.writeToFile(...)` after command
- GPA: commands call `Notebook.saveGpa()` after mutation

