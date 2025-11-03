# Goh Sze Anh - Project Portfolio Page

## Overview

Astra is a fast, keyboard-driven CLI planner for students to track academic activities (tasks, lectures, tutorials, exams) and compute their GPA. It stores data in simple text/CSV files for transparency and portability. My role focused on the entire GPA feature set, Astra's architecture, parser functionalities, key activity commands, and elevating the Developer Guide with accurate, maintainable diagrams.

## Summary of Contributions

### Code contributed
- Code Dashboard: [RepoSense dashboard](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=jeanperrieriii&breakdown=true)
- GitHub Commits: [Commits by JeanPerrierIII](https://github.com/AY2526S1-CS2113-W12-1/tp/commits/master/?author=JeanPerrierIII)

### Enhancements implemented


- Skeleton code for Command classes
  - Allowed for basic functionality of the program without actual functioning commands at the start
- dateTimeParser class
  - Enables for the flexible input of multiple date and time formats by users
  - Quality of life features:
    - Defaults to current year if not provided
    - Defaults time to 2359H if time is not provided, only for deadlines
    - `today` shortcut for current day
- Rewrote various command classes to make use of flexible date time parser, see `AddTaskCommand`, `AddExamCommand`, `AddLectureCommand`, `AddTutorialCommand`, `ChangeDeadlineCommand`
- Implemented exception handling for invalid entries within save files
  - On startup, errored or invalid entries in save files will be skipped and shown to users, with line number.
  - Makes it easier for users to determine errored entries in their saves.
- JUnitTesting for `DateTimeParser`,`CheckPriority`
- getCommandWord method
  - Seperates user input from the first word, the command word, and the rest of the input


### Contributions to the UG

- The table of contents
- Provide details on how the date and time parsing for user input works
- Provide examples on acceptable date and time formats, and when ASTRA will make assumptions
- Features or notes missed by other group members

### Contributions to the DG

- The table of contents
- User stories entries
- Authored CommandComponent diagram, style file
- Vetting, editing and consistency checking of all other member's UML diagrams
  - `ListGPAEntries`,`AddGPAEntry`,`DeleteGPAEntry`,`add_task_sequence`,`architecture_sequence_delete`,`CheckCurrent_sequence`
- Vetting of the DG 

### Contributions to team-based tasks

- Created GitHub team organisation and repo
- Set up all GitHub features as per project requirements
- Maintaining the issue tracker, including creation of tags, closing of duplicate issues, assigning members
- Review and approval of pull requests
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
    ,[PR #89](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/89)
    ,[PR #97](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/97) 
    ,[PR #99](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/99)
    ,[PR #113](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/113)
    ,[PR #176](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/176)
    ,[PR #180](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/180)
    ,[PR #181](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/181)
    ,[PR #190](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/190)

### Contributions beyond the project team:

- iP PRs reviewed
  - [[fahadmohaideen] ip](https://github.com/nus-cs2113-AY2526S1/ip/pull/99)
  - [[ChangIkJoong] iP](https://github.com/nus-cs2113-AY2526S1/ip/pull/31)
- tP PRs reviewed
  - [[CS2113-T11-2] orCASHbuddy](https://github.com/nus-cs2113-AY2526S1/tp/pull/21)