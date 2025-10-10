package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Exam;
import astra.data.Notebook;
import astra.ui.Ui;

public class CheckExamCommand extends CheckCommand {

    private ActivityList filterExams(ActivityList activities) {
        ActivityList filteredList = new ActivityList();
        for (int i = 0; i < activities.getListSize(); i++) {
            Activity activity = activities.getAnActivity(i);
            if (activity instanceof Exam) {
                filteredList.addActivity(activity);
            }
        }
        return filteredList;
    }

    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        ui.showDash();
        ActivityList filteredList = filterExams(activities);
        filteredList.listActivities();
        ui.showDash();
        return false;
    }
}
