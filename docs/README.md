# ASTRA

## Introduction

ASTRA, also known as your **A**cademic **S**cheduler, **T**ask,
**R**eminders **A**ssistant, is a CLI Chatbot which can help you track school activities such as Tutorials,
Lectures and Exams. It even helps you keep track on your tasks/assignments so that you can priorities and
plan your schedule.

New: GPA Tracker — store module grades and MCs and compute your current GPA instantly. See the User Guide for commands.

Useful links:
* [User Guide](UserGuide.md)
* [Developer Guide](DeveloperGuide.md)
* [About Us](AboutUs.md)

---

## Developer Architecture Quickstart

This is a compact overview for contributors. See the full details and UML diagrams in `DeveloperGuide.md`.

### Runtime flow
- Entry point: `astra.Astra` starts a REPL.
- Input is parsed by `astra.parser.Parser` into a `astra.command.Command`.
- `Command.execute(ActivityList, Ui, Notebook)` mutates model and prints via `Ui`.
- After each command, `Astra` persists activities using `Notebook.writeToFile(...)`.
- GPA commands persist via `Notebook.saveGpa()` to `data/gpa.txt` and `data/gpa.csv`.

### Components
- UI: `astra.ui.Ui` prints messages, help, banners.
- Parser: `astra.parser.Parser`, `DateTimeParser` (robust date/time parsing).
- Commands: `astra.command.*` (add/list/check/edit/delete, GPA add/list/delete/compute).
- Activities (model): `astra.activity.*` — `Activity`, `Task`, `Lecture`, `Tutorial`, `Exam`, `ActivityList`.
- GPA (model): `astra.gpa.*` — `GpaEntry`, `GpaList`.
- Persistence: `astra.data.Notebook` (activities + GPA IO), `astra.exception.*` (errors).

### Files and formats
- Activities (modern pipe format): lines like `TASK | 0|1 | <desc> | <YYYY-MM-DD> | <HH:MM> | <priority>`.
- GPA: `gpa.txt` → `GPA | SUBJECT | GRADE | MC`, `gpa.csv` → `Subject,Grade,MC`.

### Command map (high level)
- Create: `task ... /by ... /priority ...`, `lecture ...`, `tutorial ...`, `exam ...`, `add gpa ...`.
- List/Check: `list`, `checkexam`, `checklecture <day>`, `checktutorial <day>`, `checkcurrent [n]`, `checkpriority`, `list gpa`, `gpa`.
- Edit/Delete/Status: `delete <i...>`, `changedeadline <i> /to ...`, `changepriority <i> /to ...`, `complete <i>`, `unmark <i>`, `delete gpa <i>`.

### Developer tips
- Add a new command: create a class implementing `Command` and route it in `Parser.parse(...)`.
- Add a new activity type: subclass `Activity`/`SchoolActivity`, then update `Notebook.serializeActivity(...)` and `parseLine(...)`.
- Date/time helpers: prefer `DateTimeParser` to keep parsing consistent.
- Keep I/O centralized in `Notebook` (commands should call `writeToFile`/`saveGpa`).

### Diagrams
Rendered images are embedded in the Developer Guide. The source PlantUML blocks are kept alongside the images. If you need raw images directly:
- Architecture: `docs/images/GPA_Arch_Class_Diagram.png`
- Add/List/Delete/Compute GPA sequences and Save activity: see `docs/images/`.

