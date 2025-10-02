package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

public class ListCommand implements Command {
    //private ActivityList activities;

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        ui.showMessage("[ASTRA] This function has yet to be implemented.");
        return false;
    }
}
