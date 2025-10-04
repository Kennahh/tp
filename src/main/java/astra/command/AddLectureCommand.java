package astra.command;

import astra.activity.ActivityList;
import astra.activity.Lecture;
import astra.data.Notebook;
import astra.ui.Ui;

import java.time.LocalTime;

public class AddLectureCommand extends AddCommand {
    private String day;
    private String venue;
    private LocalTime startTime;
    private LocalTime endTime;
    private final String input;

    public AddLectureCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] parts = input.split(" ", 2);
            String args = parts[1];
            String[] details = args.split("/");
            String description = details[0].trim();
            String venue = "", day = "", startTimeStr = "", endTimeStr = "";

            for (String detail : details) {
                if (detail.startsWith("place ")) {
                    venue = detail.substring(6).trim();
                } else if (detail.startsWith("day ")) {
                    day = detail.substring(4).trim();
                } else if (detail.startsWith("from ")) {
                    startTimeStr = detail.substring(5).trim();
                } else if (detail.startsWith("to ")) {
                    endTimeStr = detail.substring(3).trim();
                }
            }

            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);

            Lecture lecture = new Lecture(description, venue, day, startTime, endTime);
            activities.addActivity(lecture);
            ui.showMessage(lecture.toString());
            
        } catch (Exception e) {
            ui.showError("Invalid lecture command format.");
        } 
        return false;
    }
}
