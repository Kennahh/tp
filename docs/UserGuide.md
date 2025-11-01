# ASTRA ChatBot

ASTRA is a **desktop app that tracks tasks, lectures, tutorials, exams and GPA, optimised for use via Command Line Interface (CLI)**

It is targetted towards students who prefer typing over graphical user interfaces, and wish to have a quick and simple way to schedule and view their timetable.

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
    - [Getting Help](#getting-help)
    - [Adding a Task](#adding-a-task)
    - [Adding a Tutorial](#adding-a-tutorial)
    - [Adding a Lecture](#adding-a-lecture)
    - [Adding an Exam](#adding-an-exam)
    - [Checking specific Activities](#checking-specific-activity)
    - [List all Activities](#list-all-activities)
    - [Deleting Activities](#deleting-activities)
    - [Completing a Task](#completing-a-task)
    - [Unmarking a Task](#unmarking-a-task)
    - [Changing Deadline](#changing-deadline-)
    - [GPA Tracker](#gpa-tracker)
- [FAQ](#faq)
- [Command Summary](#command-summary)

## Quick Start
1. Ensure that you have Java 17 or above installed.
2. Download the latest jar from GitHub releases
3. Go to the terminal and run cd into the folder containing the jar file
and run ```java -jar Astra.jar```
4. Time to start making full use of Astra to make your academic life a breeze!!!

## Features 
* All commands are case-insensitive for easier usability
* All days of the week may be input with either be spelt in full, in shorthand (minimum 3 letters), or numerical form, 1 to 7
    * e.g. `mon`, `monday`, `1`
* All dates and timings must be input in the supported formats
    * Dates may be entered in `YYYY-MM-DD` or `DD-MM-YYYY` format, seperated by either `-` or `/`, months may be represented numerically or spelt
        * e.g. `2025-12-03` or `03/12/2025` or `2025-dec-03`
    * Dates may also be entered without year, in which ASTRA will default to the current year, format must be in `DD-MM`, or if month is spelled out, either order is fine
        * e.g. `03-12`, `dec-03`, `03-december`
    * Timings may be entered in `HHmm` or `HH:mm`
        * e.g. `1300`, `13:00`
* Arguments in square brackets are optional
    * e.g. `/by <date> [time]` can be used as `/by 2025-12-03 14:00` or just `/by 2025-12-03`

### Getting Help
Ask Astra to List all the available commands the user can use as well as the input format
which is required for each command

Input:`help`

Output: Astra will print a list for all the commands available for the user to try out

### Adding a Task
Adds a new task to the ActivityList with the input **description** and deadline **date and time**.

Format: `task <description> /by <date> [time] /priority <number>`

If time is not provided, i.e. just a date, ASTRA will default to 2359H.

Example of usage: 

Input:`task tutorial assignment /by 2025-04-03 10:00 /priority 1`

Output: Astra will show the task which has been added before awaiting the next command

```
------------------------------------------------------------
[ ]tutorial assignment | Deadline: 3 Apr, 1000H | Priority: 1
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```
### Adding a Tutorial
Adds a new tutorial to the ActivityList with the input **description**, **venue**, **day of the week** and **tutorial time period**

Format:`tutorial <description> /place <venue> /day <day of week> /from <start time> /to <end time>`

Example of usage:

Input:`tutorial CS2113 T1 /place COM2-0207 /day Wednesday /from 12:00 /to 13:00`

Output:
```
------------------------------------------------------------
CS2113 T1 | Venue: COM2-0207 | Wednesday | Duration: 1200H to 1300H
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```
### Adding a Lecture
Adds a new lecture to the ActivityList with the input **description**, **venue**, **day of the week**,
 **lecture duration**

Format:`lecture <description> /place <venue> /day <day of week> /from <start time> /to <end time>`

Example of usage:

Input:`lecture CS2107 /place LT16 /day Monday /from 14:00 /to 16:00`

Output:
```
------------------------------------------------------------
CS2107 | Venue: LT16 | Monday | Duration: 1400H to 1600H
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```

### Adding an Exam
Adds a new exam to the ActivityList with the input **description**, **date**, **exam duration**

Format: `exam <description> /place <venue> /date <date> /from <time> /to <time>`

Example of usage:

Input: `exam CS2040C finals /place mpsh5 /date 2025-11-29 /from 09:00 /to 11:00`

Output:
```
------------------------------------------------------------
CS2040C finals | Venue: mpsh5 | Date: 29 Nov | Duration: 0900H to 1100H
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```

### Checking specific Activity
Prints a list of activities of the specified class and date which are in the ActivityList

Upcoming deadlines:`checkcurrent <value(optional)>` shows [value] of immediate upcoming task deadlines, default to 1

Tutorial:`checktutorial <day>` lists all tutorials on the specific day of the week

Lecture:`checklecture <day>` lists all lectures on the specific day of the week

Exam: `checkexam` lists all upcoming exams with their corresponding dates and duration

Priority: `checkpriority` list the tasks only in order of priority 

### List all Activities
Prints a list of all activities stored in the ActivityList

Example of usage:

Input:`list`

Output:
```
------------------------------------------------------------
 1. [ ]tutorial | Deadline: 3 Apr, 1000H
 2. [ ]tutorial assignment | Deadline: 3 Apr, 1000H | Priority: 1
 3. CS2113 T1 | Venue: COM2-0207 | Wednesday | Duration: 1200H to 1300H
 4. CS2107 | Venue: LT16 | Monday | Duration: 1400H to 1600H
 5. CS2040C finals | Date: 29 Nov | Duration: 0900H to 1100H
------------------------------------------------------------
```
### Deleting Activities
Deletes one or more activities at the specified index of the ActivityList

Format:`delete <index_1> <index_2> <...>`

Example of usage:

Input:`delete 1 3`

Output:
```
------------------------------------------------------------
Erased: #3 CS2113 | Venue: LT9 | Friday | Duration: 1600H to 1800H
Erased: #1 [ ]tutorial | Deadline: 3 Apr, 1000H
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```

### Completing a Task
Marks a Task at the specified index as completed

[NOTE] Only Tasks can be marked as Completed

[NOTE] After a Task has been completed, when printed in a list it will have a [x] instead of [ ]

Format:`complete <index>`

Example of usage:

Input:`complete 1`

Output:
```
------------------------------------------------------------
Marked complete: #1 [X]tutorial assignment | Deadline: 3 Apr, 1000H | Priority: 1
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```
### Unmarking a Task
Unmarks a Task at the specified index as incompleted

[NOTE] Only Tasks can be unmarked

[NOTE] After a Task has been unmarked, when printed in a list, it will show [ ] instead of [x]

Format:`unmark <index>`

Example of usage:

Input:`unmark 1`

Output:
```
------------------------------------------------------------
Unmarked: #1 [ ]tutorial assignment | Deadline: 3 Apr, 1000H | Priority: 1
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```

### Changing Deadline 
Changes the deadline of a task at the specified index

Format:`changedeadline <index> /to <date> [time]`

If time is not provided, i.e. just a date, ASTRA will default to 2359H

Example of usage:

Input:`changedeadline 1 /to 2025-10-31 14:00`

Output:
```
------------------------------------------------------------
Deadline updated for task: [ ]tutorial assignment | Deadline: 31 Oct, 1400H | Priority: 1
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```

### Changing priority
Change the priority of a specific task. The command will also adjust the priority of other affected tasks.

Format: `changepriority <task number> /to <new priority>`

Example of usage:

Input: `changepriority 1 /to 2`

Output:
```
------------------------------------------------------------
[ASTRA] Priority changed successfully for task: [ ]complete DG | Deadline: 29 Oct, 1200H | Priority: 1
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```

### GPA Tracker
Track modules and compute GPA in real-time. Grades are uppercased and S/U entries are excluded from GPA calculation.

- Add GPA entry 
  - Format: `add gpa <SUBJECT> <GRADE> <MC>`
  - Example: `add gpa CS2040C A+ 4mc`
  - Example: `add gpa ma1521 s 4` (case-insensitive)
- List GPA entries
  - Format: `list gpa`
- Compute current GPA
  - Format: `gpa`
  - Output is to 2 decimal places
- Delete a GPA entry by index
  - Format: `delete gpa <INDEX>`

Allowed grades: `A+, A, A-, B+, B, B-, C+, C, D+, D, F` (counted), `S, U` (stored but not counted).

### Storage
- Activities: `data/tasks.txt` and `data/tasks.csv`
- GPA: `data/gpa.txt` (pipe format) and `data/gpa.csv` (CSV)

Note: since all storage files update during runtime, do NOT manually edit the contents of those files during runtime. 
Users can edit before launch or after termination of astra instead.

### Exiting Astra
Exits out of the program

Example of usage:

Input:`close`

Output:
```
------------------------------------------------------------
[ASTRA] Keep up the great work! Your academic triumph awaits!
------------------------------------------------------------
```


## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: You can download your entire data folder(**Including the tasks.txt file inside**) and paste it 

into the folder of the new computer.Afterwards just download the astra.jar file  as per normal and 

run it in the same folder as data

## Command Summary
```

Adding entries to Astra:
- task <description> /by <YYYY-MM-DD> <HH:MM>
- lecture <description> /place <venue> /day <day> /from <HH:MM> /to <HH:MM>
- tutorial <description> /place <venue> /day <day> /from <HH:MM> /to <HH:MM>
- exam <description> /place <venue> /date <YYYY-MM-DD> /from <HH:MM> /to <HH:MM>
- add gpa <SUBJECT> <GRADE> <MC>

    Example: task CS2113 Quiz /by 2025-10-10 23:59
             lecture CS2113 /place LT9 /day Friday /from 16:00 /to 18:00
             tutorial CS2113 T1 /place COM2-0207 /day Wednesday /from 12:00 /to 13:00
             exam CS2107 Midterm /place mpsh1 /date 2025-10-10 /from 10:00 /to 12:00
             add gpa CS2040C A+ 4mc

Listing and Checking Tasks:
- checkcurrent [value (optional)]  (Shows [value] of immediate upcoming task deadlines, defaults to 1)
- list                             (lists all tasks only)
- checkexam                        (lists all upcoming exams with date and duration)
- checklecture <day>               (lists all lectures on a specific day)
- checktutorial <day>              (lists all tutorials on a specific day)
- list gpa                         (lists all GPA entries)
- gpa                              (computes current GPA)

Editing entries in Astra:
- delete <index_1> <index_2> <...>                      (can delete multiple tasks)
- delete gpa <INDEX>                                    (delete GPA entry)
- complete <index>                                      (mark as complete)
- unmark <index>                                        (mark as incomplete)
- changedeadline <task index> /to <YYYY-MM-DD> <HH:MM>
    Example: changedeadline 1 /to 2025-10-31 14:00

Help/Exit:
- help
- close

------------------------------------------------------------
```
