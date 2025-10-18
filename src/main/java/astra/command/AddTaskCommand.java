package astra.command;

import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.exception.InputException;

import astra.ui.Ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

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
                        "Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }

            String args = parts[1].trim();
            if (!args.contains("/by")) {
                throw new InputException("Missing '/by' keyword. Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }
            
            if (!args.contains("/priority")) {
                throw new InputException("Missing '/priority' keyword. Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }

            // Parse the arguments to extract description, deadline, and priority
            String description = "";
            String deadlineStr = "";
            int priority;

            // Split by /by first
            String[] tokens = args.split("/by", 2);
            if (tokens.length != 2) {
                throw new InputException("Invalid format. Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }

            description = tokens[0].trim();
            if (description.isEmpty()) {
                throw new InputException("Task description is missing.");
            }

            String deadlineAndPriority = tokens[1].trim();
            
            // Split by /priority to get deadline and priority
            String[] priorityParts = deadlineAndPriority.split("/priority", 2);
            if (priorityParts.length != 2) {
                throw new InputException("Invalid format. Use: task <description> /by <YYYY-MM-DD> <HH:MM> /priority <number>");
            }
            
            deadlineStr = priorityParts[0].trim();
            String priorityStr = priorityParts[1].trim();
            
            if (priorityStr.isEmpty()) {
                throw new InputException("Priority value is missing. Use: /priority <number>");
            }
            
            try {
                priority = Integer.parseInt(priorityStr);
                if (priority < 1) {
                    throw new InputException("Priority must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                throw new InputException("Priority must be a valid integer.");
            }

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

            Task task = new Task(description, deadlineDate, deadlineTime, 1); // Create with default priority
            activities.addTaskWithPriority(task, priority);
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
