package astra.parser;

import astra.command.*;
import astra.exception.InputException;

/**
 * Handles all user raw command line strings into commands.
 */
public class Parser {

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
            throw new InputException("    [ERROR] Empty wish?!. Please tell me your wish.");
        }

        String commandWord = getCommandWord(input).trim().toLowerCase();

        // further parsing and error checking of input arguments to be handled by individual commands

        switch (commandWord) {
        case "close":
            return new ExitCommand();
        case "create":
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
        default:
            throw new InputException("    [ERROR] Unrecognized command: '" + input + "'.\n" +
                    "    [ASTRA] Please use a valid command word:" +
                    "    (known commands by me, a small digital notebook: close).\n");
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
}
