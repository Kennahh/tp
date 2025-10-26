package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.ui.Ui;

public class UnmarkCommand implements Command {
    private final String input;
    public UnmarkCommand(String input) {
        this.input = input;
    }
    //private ActivityList activities;

    /**
     * Only works if the Activity is an instanceof Task.
     */
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] parts = input.split("\\s+", 2);
            if (parts.length < 2) {
                ui.showError("Please provide an index: unmark <index>");
                return false;
            }
            int index = Integer.parseInt(parts[1].trim());
            assert index > 0 : "Index should be positive";
            Activity currActivity = activities.getActivity(index - 1);
            assert currActivity != null : "Activity retrieved should not be null";
            if (!(currActivity instanceof Task)) {
                ui.showError("Activity at index " + index + " is not a Task");
                return false;
            }
            if (!((Task) currActivity).getIsComplete()) {
                ui.showError("Activity at index " + index + " is already unmarked");
                return false;
            }
            ((Task) currActivity).clearIsComplete();
            ui.showMessage("Unmarked: #" + index + " " + currActivity.toString());
        } catch (NumberFormatException e) {
            ui.showError("Index provided is not a number!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Index out of bounds.");
        }
        return false;
    }
}
