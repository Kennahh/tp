# HE Pui San - Project Portfolio Page

## Overview
Astra is a fast, keyboard-driven CLI planner for students to track academic activities (tasks, lectures, tutorials, exams) and compute their GPA. It stores data in simple text/CSV files for transparency and portability. My role focused on the entire GPA feature set, Astra's architecture, parser functionalities, key activity commands, and elevating the Developer Guide with accurate, maintainable diagrams.

## Summary of Contributions

### Code contributed
- Code Dashboard: [Kennahh-dashboard](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=kenna&sort=totalCommits%20dsc&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByNone&breakdown=true&checkedFileTypes=functional-code~other~test-code~docs&since=2025-09-19T00%3A00%3A00&filteredFileName=)
- GitHub Commits: [Commits by Kennahh](https://github.com/AY2526S1-CS2113-W12-1/tp/commits/master?author=Kennahh)

### Enhancements implemented
- Data package: Implement the storage class `Notebook.java`
  - Allow file reading at the start of the program
  - Store the data in csv file
- Some methods in activity package to support file writing in data package
  - For example: `writeToFile` method in `Activity`, `Exam`, `Lecture`, `Task`, `Tutorial`
  - Some help functions in `ActivityList` for the ease of activity retrieving
- Check exam (`CheckExamCommand`), check lecture (`CheckLecturesCommand`) and check tutorial (`CheckTutorialsCommand`)
  - Check all the exams
  - Check lectures / tutorials on a specific day
- Enhance delete command (`DeleteCommand`)
  - Enable multiple deletions of activities
- Junit tests for `DeleteCommand` and `CheckCommand`
  - Tests for `DeleteCommand` in `DeleteCommandTest.java`
  - For `CheckCommand`: write some test cases for `CheckLecturesCommand`, `CheckTutorialsCommand` and `CheckExamCommand`

### Contribution to UG
- Add checkcurrent feature to UG
- Update feature of deleting multiple activities within one command 
- Modify some example inputs and outputs
- Ensure descriptions in UG match exact behavior of the chatbot

### Contribution to DG
- Write the part relevant to check commands
- Diagrams I authored and the corresponding PNGs:
  - Check Command class diagram (`checkCommands_classDiagram.puml`)
  - Check current sequence diagram (`CheckCurrent_sequence.puml`)
  - Check lecture sequence diagram (`checkLecture_sequence.puml`)
  - Check exam sequence diagram (`checkExam_sequence.puml`)
  - Check priority sequence diagram (`checkPriority_sequence_original.puml`)
- Correct the diagrams related to `ChangePriorityCommand`

### Contribution to team-based tasks
- Help to correct code styling problems
- Add some exception and assertion
- Fix issues after PE-D

### Review/mentoring contributions
- Help review some of the PRs, for example:
  - [PR review 1](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/74)
  - [PR review 2](https://github.com/AY2526S1-CS2113-W12-1/tp/pull/51)

