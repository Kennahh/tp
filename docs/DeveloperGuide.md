# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

### GPA Tracker
The GPA tracker lets users store module entries with subject code, grade, and MCs, and computes GPA in real time.

Key classes
- Model
  - `astra.gpa.GpaEntry`
    - Fields: `subject` (uppercase, single token), `grade` (uppercase, validated), `mc` (non-negative int)
    - Methods: `gradePoints()` (maps grade to points), `isSu()` (S/U exclusion), `toPipe()`/`toCsv()` for persistence
  - `astra.gpa.GpaList`
    - Holds entries, supports `add`, `remove(1-based)`, `toList()`, and `computeGpa()` excluding S/U
- Persistence
  - `astra.data.Notebook`
    - New files: `data/gpa.txt` (pipe format) and `data/gpa.csv` (CSV)
    - On construction, loads GPA entries (best-effort) and exposes `getGpaList()` and `saveGpa()`
    - Uses try-with-resources to avoid file handle leaks (important on Windows)
- Commands
  - `AddGpaCommand` parses `add gpa <SUBJECT> <GRADE> <MC>` (MC may include `mc` suffix)
  - `ListGpaCommand` prints current GPA entries with indexes
  - `DeleteGpaCommand` deletes entry by 1-based index
  - `ComputeGpaCommand` prints GPA to 2 decimal places
- Parser
  - `Parser.parse(...)` routes:
    - `add gpa ...` → `AddGpaCommand`
    - `list gpa` → `ListGpaCommand`
    - `delete gpa <index>` → `DeleteGpaCommand`
    - `gpa` → `ComputeGpaCommand`
- UI
  - `Ui.showHelp()` updated with GPA usage and notes

Validation and rules
- Allowed grades: `A+, A, A-, B+, B, B-, C+, C, D+, D, F` (counted), `S, U` (excluded from GPA)
- GPA formula: `sum(gradePoints * mc) / sum(mc)` excluding S/U
- Subject must be a single token; both subject and grade are uppercased on storage

Error handling
- Invalid formats and grades surface user-friendly errors via `Ui.showError`
- Persistence errors throw `FileSystemException` and are caught at command level; the rest of the app continues

### Extending
- To add new grade scales, update `GpaEntry.gradePoints()` and `VALID_GRADES` in `AddGpaCommand`
- To support multi-word subjects, relax `GpaEntry` validation and extend parser to capture subject tokens

### Schedule Tracker
Check all the exams
- The mechanism is facilitated by `CheckExamCommand`. It extends `CheckCommand` abstract class.
- Command: `checkexam`
- Internal flow:
    - `CheckExamCommand#filterExam()` - Iterate through the activities list and check the type of every activity. It returns an ActivityList of all the exams in the original activities list.
    - `CheckExamCommand#execute()` - First get the filtered ActivityList from `CheckExamCommand#filterExam()`
        - If the filtered list is not empty, use `ActivityList#listActivities()` to show all the exams in the list.
        - If the filtered list is empty, show the message of no exam using `Ui#showMessage()`

Check all the lectures on a certain day
- The mechanism is facilitated by `CheckLecturesCommand`. It extends `CheckCommand` abstract class.
- Command: `checklecture <day in the week>`
- Internal flow:
    - `CheckLecturesCommand#filterActivity()` - Check whether an activity is a lecture and whether it's on the specific day.
    - `CheckLecturesCommand#filterList()` - Iterate through the activities list and pass every activity to `CheckLecturesCommand#filterActivity()`. Add the activity to the filtered list if `CheckLecturesCommand#filterActivity()` returns true. Return the filtered list of all the lectures on the specific day
    - `CheckLecturesCommand#execute()` - First get the filtered ActivityList from `CheckLecturesCommand#filterList()`
        - If the filtered list is not empty, use `ActivityList#listActivities()` to show all the lectures on the day in the list.
        - If the filtered list is empty, show the message of no lecture using `Ui#showMessage()`

Check all the tutorials on a certain day
- The mechanism is facilitated by `CheckTutorialsCommand`. It extends `CheckCommand` abstract class.
- Command: `checktutorial <day in the week>`
- Internal flow:
    - `CheckTutorialsCommand#filterActivity()` - Check whether an activity is a tutorial and whether it's on the specific day.
    - `CheckTutorialsCommand#filterList()` - Iterate through the activities list and pass every activity to `CheckTutorialsCommand#filterActivity()`. Add the activity to the filtered list if `CheckTutorialsCommand#filterActivity()` returns true. Return the filtered list of all the tutorials on the specific day
    - `CheckTutorialsCommand#execute()` - First get the filtered ActivityList from `CheckTutorialsCommand#filterList()`
        - If the filtered list is not empty, use `ActivityList#listActivities()` to show all the tutorials on the day in the list.
        - If the filtered list is empty, show the message of no tutorial using `Ui#showMessage()`


## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

| Version | As a ... | I want to ... | So that I can ...|
|---------|----------|---------------|------------------|
| v1.0    |new user|see usage instructions|refer to them when I forget how to use the application|
| v2.0    |user|find a to-do item by name|locate a to-do without having to go through the entire list|
| v2.0    |student|track my modules and GPA|know my academic standing quickly|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

GPA Tracker manual testing
- Add entries:
  - `add gpa CS2040C A+ 4mc`
  - `add gpa CFG1002 S 4`
  - Expected: success messages printed; files `data/gpa.txt` and `data/gpa.csv` updated
- List entries:
  - `list gpa` shows indexed list
- Compute GPA:
  - `gpa` prints `Current GPA: 4.25` for the above (S excluded)
- Delete entry:
  - `delete gpa 2` removes the second entry
- Invalid grade:
  - `add gpa CS1231X HH 4` shows an error
