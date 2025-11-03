package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UnmarkCommand implements Command {
    private static final Logger logger = Logger.getLogger("UnmarkCommand");
    private final String input;

    static {
        logger.setLevel(Level.OFF);
    }

    public UnmarkCommand(String input) {
        this.input = input;
    }

    /**
     * Sets the isComplete variable of the Task at the input index as false
     */
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        logger.info("Executing UnmarkCommand with input: " + input);
        try {
            String[] parts = input.split("\\s+", 2);
            if (parts.length < 2) {
                ui.showError("Please provide an index: unmark <index>");
                ui.showErrorMessage();
                logger.warning("Invalid input — no index provided");
                return false;
            }
            int index = Integer.parseInt(parts[1].trim());
            assert index > 0 : "Index should be positive";
            logger.info("Parsed index: " + index);

            Activity currActivity = activities.getActivity(index - 1);
            assert currActivity != null : "Activity retrieved should not be null";
            logger.info("Retrieved activity: " + currActivity);

            if (!(currActivity instanceof Task)) {
                ui.showError("Activity at index " + index + " is not a Task");
                ui.showErrorMessage();
                logger.warning("Activity at index " + index + " is not a Task");
                return false;
            }
            if (!((Task) currActivity).getIsComplete()) {
                ui.showError("Activity at index " + index + " is already unmarked");
                ui.showErrorMessage();
                logger.warning("Task at index " + index + " is already unmarked");

                return false;
            }
            ((Task) currActivity).clearIsComplete();
            ui.showMessage("Unmarked: #" + index + " " + currActivity.toString());
            ui.showDone();
        } catch (NumberFormatException e) {
            ui.showError("Index provided is not a number!");
            ui.showErrorMessage();
            logger.warning("Invalid index format");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Index out of bounds.");
            ui.showErrorMessage();
            logger.warning("Index out of bounds");
        }
        return false;
    }
}
