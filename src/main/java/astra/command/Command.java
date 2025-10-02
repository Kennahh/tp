package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

public interface Command {
    /**
     * Executes the command.
     * @return true if the app should exit after this command, false otherwise.
     */
    boolean execute(ActivityList activities, Ui ui, Notebook notebook);
}
