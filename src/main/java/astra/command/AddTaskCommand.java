package astra.command;

import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.exception.InputException;

import astra.parser.Parser;
import astra.ui.Ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddTaskCommand extends AddCommand {
    private final String input;

    public AddTaskCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] parts = input.split(" ", 2);
            if (parts.length != 2) {
                throw new InputException("Missing task description and deadline. " +
                        "Use: create <description> /by <YYYY-MM-DD> <HH:MM>");
            }

            String args = parts[1].trim();
            if (!args.contains("/by")) {
                throw new InputException("Missing '/by' keyword. Use: create <description> /by <YYYY-MM-DD> <HH:MM>");
            }

            String[] tokens = args.split("/by", 2);
            String description = tokens[0].trim();
            if (description.isEmpty()) {
                throw new InputException("Task description is missing.");
            }

            String deadlineStr = tokens[1].trim(); // "2025-10-10 23:59"
            if (deadlineStr.isEmpty()) {
                throw new InputException("Missing deadline entry. Use: /by <YYYY-MM-DD> <HH:MM>");
            }

            String[] deadlineParts = deadlineStr.split(" ", 2);

            
            String deadlineDateStr = deadlineParts[0];
            String deadlineTimeStr = "23:59"; // default to 2359 if no time is provided
            if (deadlineParts.length > 1) {
                deadlineTimeStr = deadlineParts[1];
            }

            LocalDate deadlineDate;
            LocalTime deadlineTime;

            try {
                deadlineDate = LocalDate.parse(deadlineDateStr);
            } catch (DateTimeParseException e) {
                throw new InputException("Invalid date format. Use: YYYY-MM-DD");
            }

            try {
                deadlineTime = LocalTime.parse(deadlineTimeStr);
            } catch (DateTimeParseException e) {
                throw new InputException("Invalid time format. Use: HH:MM");
            }

            Task task = new Task(description, deadlineDate, deadlineTime);
            activities.addActivity(task);
            ui.showMessage(task.toString());
            notebook.saveToFile(activities);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        } catch (InputException formatError) {
            ui.showError(formatError.getMessage());
        } catch (Exception e) {
            ui.showError("Invalid exam command format.");
        }
        return false;
    }
}
