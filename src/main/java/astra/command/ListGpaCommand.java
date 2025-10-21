package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.gpa.GpaEntry;
import astra.ui.Ui;

import java.util.List;

public class ListGpaCommand implements Command {
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        List<GpaEntry> list = notebook.getGpaList().toList();
        if (list.isEmpty()) {
            ui.showMessage("No GPA entries yet.");
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(" ").append(i + 1).append(". ")
              .append(list.get(i).toString()).append("\n");
        }
        ui.showMessage(sb.toString().trim());
        return false;
    }
}

