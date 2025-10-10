package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

import java.io.IOException;

public class DeleteCommand implements Command{
    private final String input;

    public DeleteCommand(String input){
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        int index;
        try {
            String arguments = input.split(" ", 2)[1];

            // todo: combine with search function when that is implemented
            index = Integer.parseInt(arguments);
            Activity removed = activities.getActivity(index - 1);
            activities.deleteActivity(index - 1);
            ui.showMessage("Erased: #" + index + " " + removed.toString());
            notebook.saveToFile(activities);
            return false;
        } catch (NumberFormatException e) {
            ui.showError("Index provided is not a number!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Activity of matching index does not exist/No index provided!");
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return false;
    }
}
