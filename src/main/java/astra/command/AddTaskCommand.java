package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

import java.time.LocalTime;

public class AddTaskCommand extends AddCommand {
    private LocalTime deadline;

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        return false;
    }
}
