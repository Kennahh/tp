package astra.parser;

import astra.command.Command;
import astra.command.DeleteCommand;
import astra.command.ExitCommand;
import astra.command.ListCommand;
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
        String commandWord = input.trim().toLowerCase();

        switch (commandWord) {
            case "close":
                return new ExitCommand();
            case "delete":
                return new DeleteCommand();
            case "list":
                return new ListCommand();
            default:
                throw new InputException("    [ERROR] Unrecognized command: '" + input + "'.\n" +
                        "    [ASTRA] Please use a valid command word:" +
                        "    (known commands by me, a small digital notebook: close).\n");
        }
    }
}
