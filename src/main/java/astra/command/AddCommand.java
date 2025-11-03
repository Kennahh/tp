package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

public abstract class AddCommand implements Command {
    protected String description;

    @Override
    public abstract boolean execute(
            ActivityList activities, Ui ui, Notebook notebook);
}
