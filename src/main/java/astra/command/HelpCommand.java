package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;


public class HelpCommand implements Command {

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        ui.showHelp();
        return false;
    }
    
}
