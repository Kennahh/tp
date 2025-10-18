package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.ui.Ui;

/**
 * Handles the "changepriority" command, which allows the user to change the priority of a task.
 * Format: changepriority {@code <task number>} /to {@code <new priority>}
 */
public class ChangePriorityCommand extends AddCommand {

    private final String input;

    public ChangePriorityCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            if (!input.contains("/to")) {
                throw new InputException(
                        "Missing '/to' keyword. Use: changepriority <task number> /to <priority>");
            }

            String[] parts = input.split(" ", 2);
            if (parts.length != 2) {
                throw new InputException(
                        "Argument missing. Use: changepriority <task number> /to <priority>");
            }

            String args = parts[1].trim();
            String[] tokens = args.split("/to", 2);
            if (tokens.length != 2) {
                throw new InputException(
                        "Task number or new priority missing. Use: changepriority <task number> /to <priority>");
            }

            int taskNumber;
            int newPriority;

            try {
                taskNumber = Integer.parseInt(tokens[0].trim());
            } catch (NumberFormatException e) {
                throw new InputException("Task number must be an integer.");
            }

            try {
                newPriority = Integer.parseInt(tokens[1].trim());
            } catch (NumberFormatException e) {
                throw new InputException("New priority must be an integer.");
            }

            assert taskNumber > 0 : "Task number should always be positive.";
            assert newPriority > 0 : "New priority should always be positive.";

            if (taskNumber > activities.getListSize()) {
                throw new InputException("Task number out of range.");
            }

            Activity activity = activities.getActivity(taskNumber - 1);
            if (!(activity instanceof Task)) {
                throw new InputException("The selected activity is not a task.");
            }

            Task task = (Task) activity;
            int oldPriority = task.getPriority();

            if (newPriority <= 0 || newPriority > activities.getListSize()) {
                throw new InputException(
                        "Priority must be between 1 and " + activities.getListSize() + ".");
            }

            if (newPriority == oldPriority) {
                throw new InputException("The task already has this priority.");
            }

            task.setPriority(newPriority);

            // Adjust other tasks' priorities accordingly
            for (int i = 0; i < activities.getListSize(); i++) {
                if (i == (taskNumber - 1)) {
                    continue;
                }

                Activity other = activities.getActivity(i);
                if (other instanceof Task) {
                    Task t = (Task) other;
                    if (oldPriority < newPriority && t.getPriority() > oldPriority
                            && t.getPriority() <= newPriority) {
                        t.setPriority(t.getPriority() - 1);
                    } else if (oldPriority > newPriority && t.getPriority() < oldPriority
                            && t.getPriority() >= newPriority) {
                        t.setPriority(t.getPriority() + 1);
                    }
                }
            }

            // Sort the list by priority
            activities.toList().sort((a, b) -> {
                if (a instanceof Task && b instanceof Task) {
                    return Integer.compare(((Task) a).getPriority(), ((Task) b).getPriority());
                }
                return 0;
            });

            // Save and confirm
            notebook.saveToFile(activities);
            ui.showMessage("[ASTRA] Priority changed successfully for task: " + task.toString());

        } catch (InputException e) {
            ui.showError(e.getMessage());
        } catch (Exception e) {
            ui.showError("Invalid changepriority command format or index.");
        }

        return false;
    }
}

