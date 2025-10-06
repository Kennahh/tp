package astra.activity;

import java.util.ArrayList;

public class ActivityList {
    private final ArrayList<Activity> ACTIVITIES;

    public ActivityList() {
        ACTIVITIES = new ArrayList<>();
    }

    /** adds activities to ArrayList*/
    public void addActivity(Activity activity) {
        ACTIVITIES.add(activity);
    }

    /** deletes task of specified index*/
    public void deleteActivity(int index) {
        ACTIVITIES.remove(index);
    }

    /** lists all activities in ArrayList */
    public void listActivities() {
        if (ACTIVITIES.isEmpty()) {
            System.out.println(" No activities have been added!");
        } else {
            for (int index = 0; index < ACTIVITIES.size(); index++) {
                System.out.print(" " + (index + 1) + ". ");
                System.out.println(ACTIVITIES.get(index).toString());
            }
        }
    }

    /**
     * Getter of the size of ACTIVITIES
     *
     * @return size of the ArrayList
     */
    public int getListSize() {
        return ACTIVITIES.size();
    }

    public Activity getAnActivity(int index) {
        return ACTIVITIES.get(index);
    }
}
