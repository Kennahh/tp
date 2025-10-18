package astra.activity;

import astra.ui.Ui;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class ActivityList {
    private final ArrayList<Activity> activities;

    public ActivityList() {
        activities = new ArrayList<>();
    }

    public Activity getActivity(int index) {
        return activities.get(index);
    }

    /**
     * adds activities to ArrayList
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * deletes task of specified index
     */
    public void deleteActivity(int index) {
        activities.remove(index);
    }

    /**
     * lists all activities in ArrayList
     */
    public void listActivities() {
        if (activities.isEmpty()) {
            System.out.println("No activities have been added!");
        } else {
            for (int index = 0; index < activities.size(); index++) {
                System.out.print(" " + (index + 1) + ". ");
                System.out.println(activities.get(index).toString());
            }
        }
    }

    /**
     * Prints all tasks in ArrayList that is due within 3 days
     */
    public void deadlineReminder() {
        Ui ui = new Ui();
        LocalDate today = LocalDate.now();
        System.out.println();
        ui.showReminderMessage();
        ui.showDash();
        int count = 0;
        for (int index = 0; index < activities.size(); index++) {
            if (getAnActivity(index) instanceof Task) {
                Task currTask = (Task) getAnActivity(index);
                long daysBetween = ChronoUnit.DAYS.between(today, currTask.getDeadlineDate());
                if (daysBetween <= 3) {
                    count += 1;
                    System.out.println(count + ". " + currTask.getDescription() + " | Days left: " + daysBetween);
                }
            }
        }
        if (count == 0) {
            System.out.println("No task due for the next 3 days");
        }
        ui.showDash();
        System.out.println();
    }

    /**
     * Getter of the size of ACTIVITIES
     *
     * @return size of the ArrayList
     */
    public int getListSize() {
        return activities.size();
    }

    public Activity getAnActivity(int index) {
        return activities.get(index);
    }

    /**
     * Provide a copy for persistence
     */
    public List<Activity> toList() {
        return new ArrayList<>(activities);
    }
}
