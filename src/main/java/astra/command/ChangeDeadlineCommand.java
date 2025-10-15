package astra.command;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.ui.Ui;

public class ChangeDeadlineCommand extends AddCommand {
    private final String input;

    public ChangeDeadlineCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            // Split input into command and arguments
            if (!input.contains("/to")) {
                throw new InputException("Missing '/to' keyword. Use: changedeadline <task number> " +
                        "/to <YYYY-MM-DD> <HH:MM>");
            }

            int taskNumber;
            String[] parts = input.split(" ", 2);
            if (parts.length != 2) {
                throw new InputException("Argument missing. Use: changedeadline <task number> " +
                        "/to <YYYY-MM-DD> <HH:MM>");
            }
            // Parse command from task index and new timestamp
            String args = parts[1].trim();
            // Parse task index from new timestamp
            String[] tokens = args.split(" /to ");

            if (tokens.length != 2) {
                throw new InputException("Task index or new timestamp missing. Use: changedeadline <task number> " +
                        "/to <YYYY-MM-DD> <HH:MM>");
            }

            String[] deadlineParts = tokens[1].split(" ");
            if (deadlineParts.length != 2) {
                throw new InputException("Date or Timestamp Missing. Use: changedeadline <task number> " +
                        "/to <YYYY-MM-DD> <HH:MM>");
            }
            String newDateStr = deadlineParts[0];
            String newTimeStr = deadlineParts[1];

            LocalDate newDate;
            LocalTime newTime;

            try {
                newDate = LocalDate.parse(newDateStr);
                newTime = LocalTime.parse(newTimeStr);
            } catch (DateTimeParseException e) {
                throw new InputException("Invalid date and/or time format. " +
                        "Use YYYY-MM-DD for date and HH:MM for time.");
            }

            try {
                taskNumber = Integer.parseInt(tokens[0].trim());
            } catch (NumberFormatException e) {
                throw new InputException("Task number must be an integer.");
            }

            // check if Task Number index is within range
            if (taskNumber <= 0 || taskNumber > activities.getListSize()) {
                throw new InputException("Task number out of range.");
            }

            // Find the task index and check if it is a Task instance
            Activity activity = activities.getActivity(taskNumber - 1);
            if (!(activity instanceof Task)) {
                throw new InputException("The selected activity is not a task.");
            }

            Task task = (Task) activity;
            task.setDeadline(newDate, newTime);
            ui.showMessage("Deadline updated for task: " + task.toString());

        } catch (InputException formatError) {
            ui.showError(formatError.getMessage());
        } catch (Exception e) {
            ui.showError("Invalid changedeadline command format or index.");
        }
        return false;
    }
}
