package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.exception.MissingArgumentException;
import astra.exception.MissingDescriptionException;
import astra.ui.Ui;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddTaskCommand extends AddCommand {
    private LocalTime deadline;
    private final String input;

    public AddTaskCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        return false;
    }

}
