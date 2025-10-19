package astra.command;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.ui.Ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CheckPriorityCommand implements Command {

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < activities.getListSize(); i++) {
            Activity activity = activities.getAnActivity(i);
            if (activity instanceof Task) {
                tasks.add((Task) activity);
            }
        }
        // Sort by priority ascending
        Collections.sort(tasks, Comparator.comparingInt(Task::getPriority));
        if (tasks.isEmpty()) {
            ui.showMessage("[ASTRA] No tasks found!");
        } else {
            ui.showMessage("[ASTRA] Tasks sorted by priority:");
            for (Task task : tasks) {
                ui.showMessage("Priority " + task.getPriority() + ": " + task.toString());
            }
        }
        return false;
    }
}

