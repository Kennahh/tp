package astra.parser;

import astra.command.AddExamCommand;
import astra.command.AddLectureCommand;
import astra.command.AddTaskCommand;
import astra.command.AddTutorialCommand;
import astra.command.ChangeDeadlineCommand;
import astra.command.CheckCurrentCommand;
import astra.command.CheckExamCommand;
import astra.command.CheckLecturesCommand;
import astra.command.CheckTutorialsCommand;
import astra.command.CompleteCommand;
import astra.command.DeleteCommand;
import astra.command.ExitCommand;
import astra.command.HelpCommand;
import astra.command.ListCommand;
import astra.command.UnmarkCommand;
import astra.command.Command;
import astra.exception.InputException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

        String commandWord = getCommandWord(input).trim().toLowerCase();

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

    public static DayOfWeek dayOfWeekParser(String input) throws InputException{

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
        day = dayMap.get(input.trim().toLowerCase().substring(0,3));
        if (day == null) {
            throw new InputException("This is not a valid day!");
        }

        return day;
    }

    /**
     * Parses date and time string provided by user into date and time format
     * @param dateAndTime user provided string
     * @return parsed user provided date and time into LocalDateTime format
     * @throws DateTimeParseException when the provided date and time does not match the expected formatting
     */
    public static LocalDateTime parseDateTime(String dateAndTime) throws DateTimeParseException {
        // append 00:00 to the end of the string if no timing is provided
        if (!dateAndTime.contains(" ")) { // no space, likely no timing, defaults to 2359H
            dateAndTime += " 2359";
        }
        // the only accepted format is ISO and whatever is written below
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HHmm");
        return LocalDateTime.parse(dateAndTime, formatter);
    }
}
