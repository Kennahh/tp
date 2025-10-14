package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



class SortByDeadline implements Comparator<Task> {
    public int compare(Task t1, Task t2) {
        LocalDateTime d1 = LocalDateTime.of(t1.getDeadlineDate(), t1.getDeadlineTime());
        LocalDateTime d2 = LocalDateTime.of(t2.getDeadlineDate(), t2.getDeadlineTime());
        return d1.compareTo(d2); // earlier deadline first
    }
}


public class CheckCurrentCommand implements Command {

    private int count;

    /*
     * Constructor for CheckCurrentCommand.
     * @param input Entire user input string.
     */
    public CheckCurrentCommand(String input) {
        String[] parts = input.split(" ");
        if (parts.length > 1) {
            try {
                this.count = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                System.out.println("[ASTRA] Defaulting to 1 upcoming task. Please enter a whole number for checkcurrent, e.g., 'checkcurrent 3'");
                this.count = 1; // fallback value
            }
        } else {
            this.count = 1;
        }
    }

    private ArrayList<Task> getClosestTasks(ActivityList activities, int count) {
        ArrayList<Task> closestTasks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < activities.getListSize(); i++) {
            Activity activity = activities.getAnActivity(i);
            if (activity instanceof Task) {
                Task temp_task = (Task) activity;
                LocalDateTime taskDeadline = LocalDateTime.of(temp_task.getDeadlineDate(), temp_task.getDeadlineTime());
                if (taskDeadline.isAfter(now)){
                    closestTasks.add((Task) activity);
                }
            }
        }
        // Sort tasks by their deadlinescheck
        Collections.sort(closestTasks, new SortByDeadline());
        // Return only the top 'count' tasks
        return new ArrayList<>(closestTasks.subList(0, Math.min(count, closestTasks.size())));
    }

    /*
     * Executes the command to check the closest task deadlines.
     * @param activities The list of activities.
     * @param ui The user interface for displaying messages.
     * @param notebook The notebook for data storage (not used in this command).
     * @return false to indicate the application should continue running.
     */
    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {

        List<Task> closestTasks = getClosestTasks(activities, this.count);
        ui.showDash();
        if (closestTasks.isEmpty()) {
            ui.showMessage("[ASTRA] No tasks found!");
        } else {
            ui.showMessage("[ASTRA] Top " + closestTasks.size() + " closest tasks:");
            for (Task task : closestTasks) {
                ui.showMessage(task.toString());
            }
        }
        ui.showDash();
        return false;
    }
    
}
