package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

public class ComputeGpaCommand implements Command {
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        assert ui != null : "Ui should not be null";
        assert notebook != null : "Notebook should not be null";
        assert notebook.getGpaList() != null : "GPA list should not be null";
        double gpa = notebook.getGpaList().computeGpa();
        assert gpa >= 0.0 && gpa <= 5.0 : "Computed GPA should be in [0.0, 5.0]";
        ui.showMessage(String.format("Current GPA: %.2f", gpa));
        ui.showDone();
        return false;
    }
}
