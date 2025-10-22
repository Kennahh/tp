package astra.command;

import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.exception.InputException;

import astra.parser.DateTimeParser;
import astra.ui.Ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

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
                throw new InputException(
                    "Missing task description and deadline. "
                    + "Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }
            String args = parts[1].trim();
            if (!args.contains("/by")) {
                throw new InputException(
                    "Missing '/by' keyword. Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }
            if (!args.contains("/priority")) {
                throw new InputException(
                    "Missing '/priority' keyword. Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }
            String[] tokens = args.split("/by", 2);
            if (tokens.length != 2) {
                throw new InputException(
                    "Invalid format. Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }
            String description = tokens[0].trim();
            if (description.isEmpty()) {
                throw new InputException(
                    "Task description is missing.");
            }
            String deadlineAndPriority = tokens[1].trim();
            String[] priorityParts = deadlineAndPriority.split("/priority", 2);
            if (priorityParts.length != 2) {
                throw new InputException(
                    "Invalid format. Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }
            String deadlineStr = priorityParts[0].trim();
            String priorityStr = priorityParts[1].trim();
            if (deadlineStr.isEmpty()) {
                throw new InputException(
                    "Missing deadline entry. Use: /by <YYYY-MM-DD> <HH:MM>");
            }
            if (priorityStr.isEmpty()) {
                throw new InputException(
                    "Priority value is missing. Use: /priority <number>");
            }   
            int priority;
            try {
                priority = Integer.parseInt(priorityStr);
                if (priority < 1) {
                    throw new InputException(
                        "Priority must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                throw new InputException(
                    "Priority must be a valid integer.");
            }
            String[] deadlineParts = deadlineStr.split(" ", 2);
            String deadlineDateStr = deadlineParts[0];
            String deadlineTimeStr = "23:59"; // default
            if (deadlineParts.length > 1) {
                deadlineTimeStr = deadlineParts[1];
            }
            LocalDate deadlineDate;
            LocalTime deadlineTime;
            try {
                deadlineDate = DateTimeParser.parseDate(deadlineDateStr);
            } catch (InputException e) {
                throw new InputException(
                    "Invalid date format. Use: YYYY-MM-DD");
            }
            try {
                deadlineTime = DateTimeParser.parseTime(deadlineTimeStr);
            } catch (InputException e) {
                throw new InputException(
                    "Invalid time format. Use: HH:MM");
            }
            Task task = new Task(description, deadlineDate, deadlineTime, 1);
            activities.addTaskWithPriority(task, priority);
            ui.showMessage(task.toString());
            notebook.saveToFile(activities);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        } catch (InputException formatError) {
            ui.showError(formatError.getMessage());
        } catch (Exception e) {
            ui.showError("Invalid task command format.");
        }
        return false;
    }
}
