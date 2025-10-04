package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.ui.Ui;

import java.time.LocalTime;

public class AddTutorialCommand extends AddCommand {
    private String day;
    private String venue;
    private LocalTime startTime;
    private LocalTime endTime;
    private final String input;

    public AddTutorialCommand(String input){
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        return false;
    }
}
