package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Lecture;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.parser.Parser;
import astra.ui.Ui;

import java.time.DayOfWeek;
import java.util.Objects;

public class CheckLecturesCommand extends CheckCommand {
    private String input;
    private DayOfWeek day;

    public CheckLecturesCommand(String input) {
        this.input = input;
    }

    /**
     * check whether an activity is an instance of Lecture
     *
     * @param activity an activity of a certain type
     * @return true if the activity is of type Lecture, false otherwise
     */
    private boolean filterActivity(Activity activity) {
        if (activity instanceof Lecture) {
            if (Objects.equals(((Lecture) activity).getDay(), day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter the activities list for lectures.
     *
     * @param activities activities list of all types of activities
     * @return an ActivityList containing lectures only
     */
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
            return false;
        }
        ActivityList filteredList = filterList(activities);
        if (filteredList.getListSize() == 0) {
            ui.showMessage("You have no lecture on " + day);
            return false;
        }
        filteredList.listActivities();
        ui.showMessage("You have " + filteredList.getListSize() + " lecture(s) on " + day);
        return false;
    }
}
