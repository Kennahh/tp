package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Tutorial;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.parser.Parser;
import astra.ui.Ui;

import java.time.DayOfWeek;
import java.util.Objects;

public class CheckTutorialsCommand extends CheckCommand {
    private String input;
    private DayOfWeek day;

    public CheckTutorialsCommand(String input) {
        this.input = input;
    }

    private boolean filterActivity(Activity activity) {
        if (activity instanceof Tutorial) {
            if (Objects.equals(((Tutorial) activity).getDay(), day)) {
                return true;
            }
        }
        return false;
    }

    private ActivityList filterList(ActivityList activities) {
        ActivityList filteredList = new ActivityList();
        for (int i = 0; i < activities.getListSize(); i++) {
            Activity activity = activities.getAnActivity(i);
            if (filterActivity(activity)) {
                filteredList.addActivity(activity);
            }
        }
        return filteredList;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            this.day = Parser.dayOfWeekParser(this.input);
        } catch (InputException e) {
            ui.showError(e.getMessage());
        }
        ActivityList filteredList = filterList(activities);
        filteredList.listActivities();
        ui.showMessage("You have " + filteredList.getListSize() + " tutorial(s) on " + day);
        return false;
    }
}
