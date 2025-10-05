package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.ui.Ui;

public class UnmarkCommand implements Command {
    private int index;
    //private ActivityList activities;

    /**
     * Only works if the Activity is an instanceof Task.
     */
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            Activity currActivity = activities.getActivity(index);
            if (!(currActivity instanceof Task)) {
                ui.showError("Activity at index " + index + " is not a Task");
                return false;
            }
            if (!((Task) currActivity).getIsComplete()) {
                ui.showError("Activity at index " + index + " is already unmarked");
                return false;
            }
            ((Task) currActivity).clearIsComplete();
            ui.showMessage("Successfully unmarked task at index " + index);
        } catch (IndexOutOfBoundsException e) {
            ui.showError("index " + index + " is out of bounds.");
        }
        return false;
    }
}
