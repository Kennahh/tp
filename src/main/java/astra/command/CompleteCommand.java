package astra.command;

import astra.activity.ActivityList;
import astra.activity.Activity;
import astra.activity.Task;
import astra.data.Notebook;
import astra.ui.Ui;

public class CompleteCommand implements Command {
    private final String input;
    public CompleteCommand(String input) {
        this.input = input;
    }

    /**
     * Only works if the Activity is an instanceof Task.
     */
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] parts = input.split("\\s+", 2);
            if (parts.length < 2) {
                ui.showError("Please provide an index: complete <index>");
                return false;
            }
            int index = Integer.parseInt(parts[1].trim());
            Activity currActivity = activities.getActivity(index-1);
            if (!(currActivity instanceof Task)) {
                ui.showError("Activity at index " + index + " is not a Task");
                return false;
            }
            if (((Task) currActivity).getIsComplete()) {
                ui.showError("Activity at index " + index + " has already completed");
                return false;
            }
            ((Task) currActivity).setIsComplete();
            ui.showMessage("Marked complete: #" + index + " " + currActivity.toString());
        } catch (IndexOutOfBoundsException e) {
            ui.showError("index out of bounds.");
        }
        return false;
    }
}
