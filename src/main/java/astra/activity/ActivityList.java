package astra.activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityList {
    private final ArrayList<Activity> activities;

    public ActivityList() {
        activities = new ArrayList<>();
    }

    public Activity getActivity(int index){
        return activities.get(index);
    }

    /** adds activities to ArrayList*/
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /** deletes task of specified index*/
    public void deleteActivity(int index) {
        activities.remove(index);
    }

    /** lists all activities in ArrayList */
    public void listActivities() {
        if (activities.isEmpty()) {
            System.out.println(" No activities have been added!");
        } else {
            for (int index = 0; index < activities.size(); index++) {
                System.out.print(" " + (index + 1) + ". ");
                System.out.println(activities.get(index).toString());
            }
        }
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

    /** Provide a copy for persistence */
    public List<Activity> toList() {
        return new ArrayList<>(activities);
    }
}
