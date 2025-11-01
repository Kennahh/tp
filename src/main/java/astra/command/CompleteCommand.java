package astra.command;

import astra.activity.ActivityList;
import astra.activity.Activity;
import astra.activity.Task;
import astra.data.Notebook;
import astra.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CompleteCommand implements Command {
    private static final Logger logger = Logger.getLogger("CompleteCommand");
    private final String input;

    static {
        // Set level of logging, OFF disables it entirely
        logger.setLevel(Level.OFF);
    }

    public CompleteCommand(String input) {
        this.input = input;
    }

    /**
     * Sets the isComplete variable of the Task at the input index as true
     */
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        logger.info("Executing CompleteCommand with input: " + input);
        try {
            String[] parts = input.split("\\s+", 2);
            if (parts.length < 2) {
                ui.showError("Please provide an index: complete <index>");
                logger.warning("Invalid input - no index provided");

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
                logger.warning("Activity at index " + index + " is not a Task");
                return false;
            }
            if (((Task) currActivity).getIsComplete()) {
                ui.showError("Activity at index " + index + " is already completed");
                logger.warning("Task at index " + index + " is already completed");
                return false;
            }
            ((Task) currActivity).setIsComplete();
            ui.showMessage("Marked complete: #" + index + " " + currActivity.toString());
            logger.info("Task at index " + index + " has been marked as completed");
        } catch (NumberFormatException e) {
            ui.showError("Index provided is not a number!");
            logger.warning("Invalid index format");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("index out of bounds.");
            logger.warning("Index out of bounds");
        }
        return false;
    }
}
