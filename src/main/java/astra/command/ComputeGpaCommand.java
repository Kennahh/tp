package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

public class ComputeGpaCommand implements Command {
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        double gpa = notebook.getGpaList().computeGpa();
        ui.showMessage(String.format("Current GPA: %.2f", gpa));
        return false;
    }
}

