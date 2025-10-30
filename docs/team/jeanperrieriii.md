# Goh Sze Anh - Project Portfolio Page

## Overview

Astra is a fast, keyboard-driven CLI planner for students to track academic activities (tasks, lectures, tutorials, exams) and compute their GPA. It stores data in simple text/CSV files for transparency and portability. My role focused on the entire GPA feature set, Astra's architecture, parser functionalities, key activity commands, and elevating the Developer Guide with accurate, maintainable diagrams.

## Summary of Contributions

### Code contributed
- Code Dashboard: [RepoSense dashboard](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=jeanperrieriii&breakdown=true)
- GitHub Commits: [Commits by JeanPerrierIII](https://github.com/AY2526S1-CS2113-W12-1/tp/commits/master/?author=JeanPerrierIII)

### Enhancements implemented


- Flexible date time parser
  - Enables for the input of multiple date and time formats by users
  - Quality of life features:
    - Defaults to current year if not provided
    - Defaults time to 2359H if time is not provided, only for deadlines
    - `today` shortcut for current day
  - Utilised by `AddTaskCommand`, `AddExamCommand`, `AddLectureCommand`, `AddTutorialCommand`, `ChangeDeadlineCommand`
- JUnitTesting for `DateTimeParser`,`CheckPriority`

- Detailed exception handling for detailed user feedback on wrong usage


### Contributions to the UG

- Provide details on how the date and time parsing for user input works
- Provide examples on acceptable date and time formats, and when ASTRA will make assumptions

### Contributions to the DG

- User stories entries
- Authored CommandComponent diagram, style file
- Vetting, editing and consistency checking of all other member's UML diagrams
  - `ListGPAEntries`,`AddGPAEntry`,`DeleteGPAEntry`,`add_task_sequence`,`architecture_sequence_delete`
- Vetting of the DG 

### Contributions to team-based tasks

- Created GitHub team organisation and repo
- Set up all GitHub features as per project requirements
- Maintaining the issue tracker, including creation of tags, closing of duplicate issues, assigning members
- Manage releases
- Update README.md

### Review/mentoring contributions:

- Assist teammates with various odd errors
  - [PR #53](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/53)
    - The gradle.yml file had been accidentally deleted and nobody knew
  - [PR #110](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/110)
    - Capitalisation of filenames led to conflicts on some teammates' systems
- PRs reviewed
  - [PR #84](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/84)
  - [PR #89](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/89)
  - [PR #97](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/97) 
  - [PR #99](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/99)

### Contributions beyond the project team: