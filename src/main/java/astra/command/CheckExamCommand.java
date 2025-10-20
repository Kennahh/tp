package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Exam;
import astra.data.Notebook;
import astra.ui.Ui;

public class CheckExamCommand extends CheckCommand {

    /**
     * Filter the activities list for exams.
     *
     * @param activities activities list of all types of activities
     * @return an ActivityList containing exams only
     */
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
        ActivityList filteredList = filterExams(activities);
        if (filteredList.getListSize() == 0) {
            ui.showMessage("No exams in your list!");
            return false;
        }
        filteredList.listActivities();
        return false;
    }
}
