package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

import java.time.LocalTime;

public class AddExamCommand extends AddCommand {
    private LocalTime startTime;
    private LocalTime endTime;
    private final String input;

    public AddExamCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        return false;
    }
}
