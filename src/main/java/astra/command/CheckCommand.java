package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

public abstract class CheckCommand implements Command {
    //private ActivityList activities;

    @Override
    public abstract boolean execute(ActivityList activities, Ui ui, Notebook notebook);
}
