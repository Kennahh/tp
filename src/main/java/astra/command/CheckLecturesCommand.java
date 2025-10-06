package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Lecture;
import astra.data.Notebook;
import astra.ui.Ui;

import java.util.Objects;

public class CheckLecturesCommand extends CheckCommand {
    private String day;

    public CheckLecturesCommand(String day) {
        this.day = day;
    }

    private boolean filterActivity(Activity activity) {
        if (activity instanceof Lecture) {
            if (Objects.equals(((Lecture) activity).getDay(), day)) {
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
        ActivityList filteredList = filterList(activities);
        filteredList.listActivities();
        ui.showMessage("You have " + filteredList.getListSize() + " lecture(s) on " + day);
        return false;
    }
}
