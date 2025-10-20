package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.ui.Ui;

import java.io.IOException;
import java.util.Arrays;

public class DeleteCommand implements Command{
    private final String input;

    public DeleteCommand(String input){
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        int index;
        try {
            if (input.split(" ").length <= 1) {
                throw new InputException("Task number can't be empty!");
            }
            String arguments = input.split(" ", 2)[1];
            String[] taskNumbers = arguments.split(" ");
            assert taskNumbers.length >= 1: "There should be at least one task number.";
            int[] numbers = new int[taskNumbers.length];
            for (int i = 0; i < taskNumbers.length; i++) {
                String taskNumber = taskNumbers[i];
                index = Integer.parseInt(taskNumber);
                numbers[i] = index;
            }
            Arrays.sort(numbers);
            for (int i = numbers.length - 1; i >= 0; i--) {
                index = numbers[i];
                Activity removed = activities.getActivity(index - 1);
                activities.deleteActivity(index - 1);
                ui.showMessage("Erased: #" + index + " " + removed.toString());
            }
            notebook.saveToFile(activities);
            return false;
        } catch (NumberFormatException e) {
            ui.showError("Index provided is not a number!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Activity of matching index does not exist/No index provided!");
        } catch (IOException e) {
            ui.showError(e.getMessage());
        } catch (InputException e) {
            ui.showError(e.getMessage());
        }
        return false;
    }
}
