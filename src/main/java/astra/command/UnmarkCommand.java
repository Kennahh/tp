package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

public class UnmarkCommand implements Command {
    private int index;
    //private ActivityList activities;

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        return false;
    }
}
