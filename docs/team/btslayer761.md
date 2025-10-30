# Brendan Tey - Project Portfolio Page

## Overview
Astra is a fast, keyboard-driven CLI planner for students to track academic activities (tasks, lectures, tutorials, exams) and compute their GPA. It stores data in simple text/CSV files for transparency and portability. My role focused on the entire GPA feature set, Astra's architecture, parser functionalities, key activity commands, and elevating the Developer Guide with accurate, maintainable diagrams.

## Summary of Contributions
### Code DashBoard: ([BTslayer761-dashboard-link](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=Bren&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Kurokishi592&tabRepo=AY2526S1-CS2113-W12-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false))
### GitHub commits: ([BTslayer761-commits](https://github.com/AY2526S1-CS2113-W12-1/tp/commits/master?author=BTslayer761))
### Enhancements implemented: 
- Activities and Command Package skeleton codes
  - Implemented the skeleton codes and overall structure of the activity classes and the command classes
  - Allowed the structure of the project to be established before further implementation were done
- UnmarkCommand / CompleteCommand
  -  Parsing of Unmark and Complete Command inputs
  - Handled errors/exceptions caused by
    1. Wrong input format
    2. User giving an input other than a number
    3. Number input received exceeds list size (out of bound)
    4. Activity at the index not being a Task 
    5. When unmarking/Completing a Task that is already unmarked/completed
- deadlineReminder method
  - List Tasks which are due within the next 3 days from when the user runs Astra
  - Takes in the date the program is being run and compares it with all Activities in ActivityList
  - Filters out all tsk objects and compare their deadline with the current date.
  - Makes it easy for Users to remember which tasks are due soon as well as give them buffer to complete the task (3 days)
  - Handled different output for when there are activities due soon and when there aren't any
- listAndDeleteOverdueTasks method
  - Goes through the ActivityList and delete tasks which have deadlines pass the current day Astra is running
  - Implemented different counters to ensure the only indexes not out of bounds are checked since the remove method dynamically updates the ArrayList (activities)
  - Ensures that that no out of bounds error is thrown
  - Makes it easier for Users to manage their ActivityLists as automates deletion of overdue tasks

### Contribution to UG:
- Wrote the UG presented for V1.0. 
- Wrote most of the UG as well as formatted it for easier readability for all functions presented at V1.0
- Includes commands for all Add commands (excluding GPA), Check Commands, Update commands and command summary for methods presented for v1.0


### Contribution to DG:
- Sections i did were the activity Package and the Unmark/Complete Commands
- Diagrams Created include:
  1. Activity Class diagram
  2. DeleteCommand sequence diagram
  3. UnmarkCommand sequence diagram
  4. Edited checkCurrent sequence diagram
  5. AddCommands sequence diagram

### Contribution to team-based tasks
- Ensured coding quality and correct styling
- Assisted in checking for bugs through the implementation of JUnit tests

### Review/mentoring contributions: 
- Reviewed DG implementation and gave feedback on how we could organise the DG [Example of review](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/95)
- Help identify errors in codes/JavaDoc
- Reviewed JUnit tests formatting.

### Contributions beyond the project team
- Reviewed DG uml diagrams of T10-1(FinSight)
