package astra.parser;

import astra.command.AddExamCommand;
import astra.command.AddLectureCommand;
import astra.command.AddTaskCommand;
import astra.command.AddTutorialCommand;
import astra.command.ChangeDeadlineCommand;
import astra.command.ChangePriorityCommand;
import astra.command.CheckCurrentCommand;
import astra.command.CheckExamCommand;
import astra.command.CheckLecturesCommand;
import astra.command.CheckTutorialsCommand;
import astra.command.CheckPriorityCommand;
import astra.command.CompleteCommand;
import astra.command.DeleteCommand;
import astra.command.DeleteGpaCommand;
import astra.command.ExitCommand;
import astra.command.HelpCommand;
import astra.command.ListCommand;
import astra.command.ListGpaCommand;
import astra.command.UnmarkCommand;
import astra.command.Command;
import astra.command.AddGpaCommand;
import astra.command.ComputeGpaCommand;
import astra.exception.InputException;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles all user raw command line strings into commands.
 */
public class Parser {
    private static final Map<String, DayOfWeek> dayMap = new HashMap<>();

    /**
     * Parses a line of user input.
     *
     * @param input Raw input line.
     * @return Command instance to execute.
     * @throws InputException If input is empty or command is unknown.
     */
    public static Command parse(String input) throws InputException {
        if (input == null) {
            throw new InputException("    [ERROR] Oh no your wish is an empty input! Please tell me your wish.");
        }
        if (input.trim().isEmpty()) {
            throw new InputException("    [ERROR] Empty wish?! Please tell me your wish :(");
        }

        String trimmed = input.trim();
        String commandWord = getCommandWord(trimmed).trim().toLowerCase();

        // GPA special-case commands that share base words
        if (commandWord.equals("add") && trimmed.toLowerCase().startsWith("add gpa")) {
            return new AddGpaCommand(trimmed);
        }
        if (commandWord.equals("list") && trimmed.toLowerCase().startsWith("list gpa")) {
            return new ListGpaCommand();
        }
        if (commandWord.equals("delete") && trimmed.toLowerCase().startsWith("delete gpa")) {
            return new DeleteGpaCommand(trimmed);
        }
        if (commandWord.equals("gpa")) {
            return new ComputeGpaCommand();
        }

        // further parsing and error checking of input arguments to be handled by individual commands

        switch (commandWord) {
        case "close":
            return new ExitCommand();
        case "task":
            return new AddTaskCommand(input);
        case "lecture":
            return new AddLectureCommand(input);
        case "tutorial":
            return new AddTutorialCommand(input);
        case "exam":
            return new AddExamCommand(input);
        case "delete":
            return new DeleteCommand(input);
        case "list":
            return new ListCommand();
        case "help":
            return new HelpCommand();
        case "changedeadline":
            return new ChangeDeadlineCommand(input);
        case "complete":
            return new CompleteCommand(input);
        case "unmark":
            return new UnmarkCommand(input);
        case "checkexam":
            return new CheckExamCommand();
        case "checkcurrent":
            return new CheckCurrentCommand(input);
        case "checkpriority":
            return new CheckPriorityCommand();
        case "changepriority":
            return new ChangePriorityCommand(input);
        case "checklecture": {
            String[] parts = input.split("\\s+", 2);
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new InputException("Please provide a day: checklecture <day>");
            }
            return new CheckLecturesCommand(parts[1].trim());
        }
        case "checktutorial":
            String[] parts = input.split("\\s+", 2);
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new InputException("Please provide a day: checktutorial <day>");
            }
            return new CheckTutorialsCommand(parts[1].trim());
        default:
            throw new InputException("    [ERROR] Unrecognized command: '" + input + "'.\n" +
                    "    [ASTRA] Please use a valid command word:" +
                    "    (use `help` to check known commands by me, a small digital notebook :( ).\n");
        }

    }

    /**
     * Splits the raw user input into the first word (the command) and the rest (the arguments)
     *
     * @param input raw user input string
     * @return just the first word, the command
     */
    private static String getCommandWord(String input) {
        String[] splitString = input.split(" ", 2);
        return splitString[0];
    }

    static {
        for (DayOfWeek day : DayOfWeek.values()) {
            String full = day.name().toLowerCase();  // e.g. "monday"
            String shortForm = full.substring(0, 3); // e.g. "mon"
            dayMap.put(shortForm, day);
        }
    }

    public static DayOfWeek dayOfWeekParser(String input) throws InputException {

        String sanitisedInput = input.trim().toLowerCase();

        if (sanitisedInput.isEmpty()) {
            throw new InputException("Day string cannot be null");
        }
        // input not empty

        if (input.matches("[1-7]")) {
            int number = Integer.parseInt(sanitisedInput);
            return DayOfWeek.of(number);
        }

        if (input.trim().length() < 3) {
            throw new InputException("Provide at least 3 letters to specify day");
        }
        // input is at least 3 letters long
        DayOfWeek day;
        day = dayMap.get(input.trim().toLowerCase().substring(0, 3));
        if (day == null) {
            throw new InputException("This is not a valid day!");
        }

        return day;
    }
}
