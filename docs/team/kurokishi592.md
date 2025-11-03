# Kenneth Wong — Project Portfolio Page

## Overview

Astra is a fast, keyboard-driven CLI planner for students to track academic activities (tasks, lectures, tutorials, exams) and compute their GPA. It stores data in simple text/CSV files for transparency and portability. My role focused on the entire GPA feature set, Astra's architecture, parser functionalities, key activity commands, and elevating the Developer Guide with accurate, maintainable diagrams.

## Summary of Contributions

### Code contributed

- Code Dashboard: [kurokishi592-dashboard-link](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=kuro&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Kurokishi592&tabRepo=AY2526S1-CS2113-W12-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
- GitHub commits: [kurokishi592-github-commits](https://github.com/AY2526S1-CS2113-W12-1/tp/commits/master?author=Kurokishi592)
- Commit volume (since 2025-09-19): 31 commits under "Kurokishi592" (+ 8 under alias "Kenneth Wong"). Lines changed (approx.): +3792 additions, -1032 deletions.

### Enhancement Implemented 

- GPA Tracker (core feature)
	- Commands: `AddGpaCommand`, `ListGpaCommand`, `DeleteGpaCommand`, `ComputeGpaCommand` with robust token validation and friendly error messages.
	- Model: `GpaEntry` (input normalization + validation; S/U excluded from GPA) and `GpaList` (immutable view via `toList`, defensive assertions, `computeGpa`).
	- Persistence: integrated GPA read/write into `Notebook` with both pipe format (`gpa.txt`) and CSV (`gpa.csv`) for interoperability.
	- Other design choices: 1-based indexing and explicit error handling via `GpaInputException` for consistency with the rest of the app.

- Astra's architecture
    - determined the high-level overview of Astra’s components and how they interact in a typical command flow. (`Astra → Parser → Command → Model/Storage → Ui`).
    - established the REPL flow in `Astra`: `parse → execute → persist` on non-exit.
        - for better Separation of Concerns, unit testability and extensibility:
            - each component has one responsibility: Parser understands language, Command implements behaviour, Model holds state and invariants, Notebook persists, Ui handles I/O. Commands expose behaviour in methods that can be called for unit tests. The REPL orchestration stays short and obvious. New contributors can quickly find where to add new commands or fix parsing without tracing tangled code. Adding features is straightforward: implement a new Command subclass + parser entry. Example: GPA features were added as new commands (`AddGpaCommand`, `ComputeGpaCommand`) and integrated without rewriting the REPL loop.

- JUnit test codes
    - Achieved around 80% test coverage for all packages by class, method, line and branch

### Contributions to the User Guide (UG)

- Added guides for GPA Tracker, including all the functions, input format, example inputs and expected outputs.

### Contributions to the Developer Guide (DG)
- Authored the following DG contents to guide current/future developers through Astra’s architecture, design and implementation.
    - Design (`Architecture, Parser, Commands, Activity, Gpa, Storage, Ui, Exceptions`) - Implementation.
        - `Activities` (Add Task with priority, Change priority rebalancing, Check current tasks) and `Gpa Tracker`
- Authored diagrams (PlantUML + exported PNGs) and integrated them near relevant prose, with notable ones such as: `main_loop_sequence.puml`, `architecture_component.puml`, `architecture_sequence_delete.puml`, `model_component.puml`, `add_task_sequence.puml`, and all GPA architecture and sequence diagrams such as `AddGPAEntry.puml`, `ComputeGPA.puml`, `ListGPAEntries.puml`, `DeleteGPAEntry.puml`.
- Added/updated manual testing instructions for GPA and activities (Appendix E) and usage examples to complement GPA commands.

Developer Guide is under `docs/DeveloperGuide.md` and PlantUML sources under `docs/diagrams/` with exported PNGs in `docs/images/`

### Contributions to team-based tasks

- Maintaining the issue tracker
- Ensure code quality, code standards and no styling errors
    - logging, exceptions and assertions, coding quality and standards, SLAP, no duplicated codes

### Review/mentoring contributions

- [PR #38](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/38): Assisted teammates in debugging parsing and priority edge cases:
- [PR #57](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/57): Ensure code quality, fix all checkstyle errors and bug
- [PR #4](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/4), [PR #38](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/38), [PR #51](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/51), [PR #89](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/89): Selected PRs authored/merged (evidence of contributions, documentation and merges)