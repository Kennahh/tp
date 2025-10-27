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

    /**
     * check whether an activity is an instance of Tutorial
     *
     * @param activity an activity of a certain type
     * @return true if the activity is of type Tutorial, false otherwise
     */
    private boolean filterActivity(Activity activity) {
        if (activity instanceof Tutorial) {
            if (Objects.equals(((Tutorial) activity).getDay(), day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter the activities list for tutorials.
     *
     * @param activities activities list of all types of activities
     * @return an ActivityList containing tutorials only
     */
    private ActivityList filterList(ActivityList activities) {
        ActivityList filteredList = new ActivityList();
        for (int i = 0; i < activities.getListSize(); i++) {
            Activity activity = activities.getAnActivity(i);
            if (filterActivity(activity)) {
                filteredList.addActivity(activity);
            }
        }
        assert filteredList.getListSize() <= activities.getListSize(): "Edge case: all activities in the list are tutorials " +
                "on " + day;
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
        if (filteredList.getListSize() == 0) {
            ui.showMessage("You have no tutorial on " + day);
            return false;
        }
        filteredList.listActivities();
        ui.showMessage("You have " + filteredList.getListSize() + " tutorial(s) on " + day);
        return false;
    }
}
