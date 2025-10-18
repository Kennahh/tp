# ASTRA ChatBot



## Quick Start



1. Ensure that you have Java 17 or above installed.
2. Download the latest jar from GitHub releases
3. Go to the terminal and run cd into the folder containing the jar file
and run ```java -jar Astra.jar```
4. Time to start making full use of Astra to make your academic life a breeze!!!

## Features 
* All commands are case-insensitive for easier usability
* All days of the week must be spelt in full
* All timings must follow the format as stated

### Getting Help
Ask Astra to List all the available commands the user can use as well as the input format
which is required for each command

Input:`help`

Output: Astra will print a list for all the commands available for the user to try out

### Adding a Task
Adds a new task to the ActivityList with the input **description** and deadline **date and time**.

Format: `task <description> /by <YYY-MM-DD> <HH:mm>`

Example of usage: 

Input:`task tutorial assignment /by 2025-04-03 10:00`

Output: Astra will show the task which has been added before awaiting the next command

```
------------------------------------------------------------
[ ]tutorial assignment | Deadline: 3 Apr, 1000H
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

Format: `exam <description> /place <venue> /date <YYYY-MMM-DD> /from <HH:mm> /to <HH:mm>`

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

Task: `list` lists all tasks only

Tutorial:`checktutorial <day>` lists all tutorials on the specific day of the week

Lecture:`checklecture <day>` lists all lectures on the specific day of the week

Exam: `checkexam` lists all upcoming exams with their corresponding dates and duration

### List all Activities
Prints a list of all activities stored in the ActivityList

Example of usage:

Input:`list`

Output:
```
------------------------------------------------------------
 1. [ ]tutorial | Deadline: 3 Apr, 1000H
 2. [ ]tutorial assignment | Deadline: 3 Apr, 1000H
 3. CS2113 T1 | Venue: COM2-0207 | Wednesday | Duration: 1200H to 1300H
 4. CS2107 | Venue: LT16 | Monday | Duration: 1400H to 1600H
 5. CS2040C finals | Date: 29 Nov | Duration: 0900H to 1100H
------------------------------------------------------------
```
### Deleting an Activity
Deletes an activity at the specified index of the ActivityList

Format:`delete <index>`

Example of usage:

Input:`delete 1`

Output:
```
------------------------------------------------------------
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
Marked complete: #1 [X]tutorial assignment | Deadline: 3 Apr, 1000H
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
Unmarked: #1 [ ]tutorial assignment | Deadline: 3 Apr, 1000H
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```

### Changing Deadline 
Changes the deadline of a task at the specified index

Format:`changedeadline <index> /to <YYYY-MM-DD> <HH:mm>`

Example of usage:

Input:`changedeadline 1 /to 2025-10-31 14:00`

Output:
```
------------------------------------------------------------
Deadline updated for task: [ ]tutorial assignment | Deadline: 31 Oct, 1400H
------------------------------------------------------------
[ASTRA] Done! Now, what's your next wish...
```

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

    Example: task CS2113 Quiz /by 2025-10-10 23:59
             lecture CS2113 /place LT9 /day Friday /from 16:00 /to 18:00
             tutorial CS2113 T1 /place COM2-0207 /day Wednesday /from 12:00 /to 13:00
             exam CS2107 Midterm /place mpsh1 /date 2025-10-10 /from 10:00 /to 12:00

Listing and Checking Tasks:
- checkcurrent [value (optional)]  (Shows [value] of immediate upcoming task deadlines, defaults to 1)
- list                             (lists all tasks only)
- checkexam                        (lists all upcoming exams with date and duration)
- checklecture <day>               (lists all lectures on a specific day)
- checktutorial <day>              (lists all tutorials on a specific day)

Editing entries in Astra:
- delete <index>
- complete <index>                                      (mark as complete)
- unmark <index>                                        (mark as incomplete)
- changedeadline <task index> /to <YYYY-MM-DD> <HH:MM>
    Example: changedeadline 1 /to 2025-10-31 14:00

Help/Exit:
- help
- close

------------------------------------------------------------
```

