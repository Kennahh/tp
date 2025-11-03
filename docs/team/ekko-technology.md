# ekko-technology — Project Portfolio Page

## Overview

Astra is a fast, keyboard-driven CLI planner for students to track academic activities (tasks, lectures, tutorials, exams) and compute their GPA. It stores data in simple text/CSV files for transparency and portability. My role focused on mainly Task-related activity and command, its Deadline & Priority index management, parser functionalities and elevating the Developer Guide with accurate, maintainable diagrams.

## Summary of Contributions

### Code contributed

- Code Dashboard: ([ekko-technology-dashboard-link](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=ekko&sort=totalCommits%20dsc&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByNone&breakdown=true&checkedFileTypes=functional-code~other~test-code~docs&since=2025-09-19T00%3A00%3A00&filteredFileName=))

- GitHub commits: ([ekko-technology-github-commits](https://github.com/AY2526S1-CS2113-W12-1/tp/commits/master?author=Ekko-Technology&after=4c1099ebbc3fd8d8f4204742700d8897518b15ae+34)) 


### Enhancements implemented

- Add commands (create flows)
  - `AddTaskCommand`: full parsing and validation for the `task <desc> /by <YYYY-MM-DD> <HH:MM> /priority <n>` form, bounds-checking for priority (1..taskCount+1), shifting existing priorities on insert, and persistence to `Notebook`.
  - `AddExamCommand`, `AddLectureCommand`, `AddTutorialCommand`: implemented/maintained parsing and validation for required fields (dates/times/place/day), and ensured consistent persistence behavior.

- Change priority (`ChangePriorityCommand`)
  - Implemented priority rebalancing algorithm that updates numeric priority labels of affected tasks so the label set remains contiguous (1..N). Added validation for missing flags, non-integer input, out-of-range values, non-task selection, and same-priority no-ops.
  - Ensured the command updates numeric labels only and preserves the `ActivityList` ordering (indices remain stable).

- Change deadline (`ChangeDeadlineCommand`)
  - Implemented robust date/time parsing and validation for deadline updates, preserved task state, updated deadline fields, and persisted changes.

### Contributions to the User Guide (UG)

- Added usage examples and error-handling notes for activity creation commands (`task`, `exam`, `lecture`, `tutorial`) and for `changedeadline` / `changepriority` flows. These help end users understand valid input forms and expected error messages.

### Contributions to the Developer Guide (DG)

- Authored and updated several PlantUML sequence diagrams and their exported PNGs used in the Developer Guide. Key diagrams added/updated:
  - `docs/diagrams/checkCurrent_sequence.puml` → `docs/images/checkCurrent_sequence.png`
  - `docs/diagrams/checkPriority_sequence.puml` → `docs/images/checkPriority_sequence.png`
  - `docs/diagrams/changePriority_sequence.puml` → `docs/images/changePriority_sequence.png`
  - `docs/diagrams/ChangeDeadline_sequence.puml` → `docs/images/changeDeadline_sequence.png`

### Contributions to team-based tasks

- Helped debug parsing and priority edge cases across the codebase.
- Assisted in creation of additional Junit tests for teammates
- Ensure code quality, code standards and no styling errors

### Review / mentoring contributions

- Reviewed PRs related to activity parsing and priority implementation; provided feedback on error messages, assertions, and test coverage.

- PRs reviewed
  - [PR #82](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/82)
    - Assisted in flagging out redundant exception checks to make code more concise
    - Ensure code adopted CS2113 code style.
  - [PR #22](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/22)
    - Ensure code adopted CS2113 code style.


