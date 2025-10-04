package astra.command;

import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.exception.InputException;

import astra.parser.Parser;
import astra.ui.Ui;

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
            String args = parts[1];
            String[] tokens = args.split(" /by ");
            String description = tokens[0].trim();
            String deadlineStr = tokens[1].trim(); // "2025-10-10 23:59"

            String[] deadlineParts = deadlineStr.split(" ");
            String deadlineDateStr = deadlineParts[0];
            String deadlineTimeStr = deadlineParts[1];

            LocalDate deadlineDate = LocalDate.parse(deadlineDateStr);
            LocalTime deadlineTime = LocalTime.parse(deadlineTimeStr);

            Task task = new Task(description, deadlineDate, deadlineTime);
            activities.addActivity(task);
            ui.showMessage(task.toString());
        } catch (Exception e) {
            ui.showError("Invalid task command format.");
        }
        return false;
    }
}
