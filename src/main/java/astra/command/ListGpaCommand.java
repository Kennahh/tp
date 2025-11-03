package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.gpa.GpaEntry;
import astra.ui.Ui;

import java.util.List;

public class ListGpaCommand implements Command {
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        assert ui != null : "Ui should not be null";
        assert notebook != null : "Notebook should not be null";
        assert notebook.getGpaList() != null : "GPA list should not be null";
        List<GpaEntry> list = notebook.getGpaList().toList();
        assert list != null : "Returned GPA list should not be null";
        if (list.isEmpty()) {
            ui.showMessage("No GPA entries yet.");
            ui.showErrorMessage();
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            assert list.get(i) != null : "GPA entry should not be null";
            sb.append(i + 1).append(". ")
              .append(list.get(i).toString()).append("\n");
        }
        ui.showMessage(sb.toString().trim());
        ui.showDone();
        return false;
    }
}
