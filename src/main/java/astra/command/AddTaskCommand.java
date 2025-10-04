package astra.command;

import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.exception.InputException;

import astra.parser.Parser;
import astra.ui.Ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class AddTaskCommand extends AddCommand {
    private final String input;

    public AddTaskCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] splitInput = inputHandler();

            LocalDateTime byDate = Parser.parseDateTime(splitInput[1].trim());
            Task temp = new Task(splitInput[0].trim(), byDate);
            activities.addActivity(temp);
            System.out.println("Task " + temp + " has been added!");

        } catch (InputException e) {
            ui.showError(e.getMessage());
        } catch (DateTimeParseException e) {
            ui.showError("Error: date format incorrect, use yyyy/mm/dd hhmm");
        }
//        ui.printCorrectUsage(CommandType.DEADLINE, false);
        return false;
    }

    private String[] inputHandler() throws InputException {
        String[] splitInput;

        if (!input.contains("/by")) {
            throw new InputException("Insufficient arguments!");
        }

        splitInput = input.split("/by", 2);

        if (splitInput[0].trim().isEmpty()) {
            throw new InputException("Description cannot be empty!");
        }

        if (splitInput[1].trim().isEmpty()) {
            throw new InputException("Deadline cannot be empty!");
        }
        return splitInput;
    }
}
