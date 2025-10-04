package astra.command;

import astra.activity.ActivityList;
import astra.activity.Exam;
import astra.data.Notebook;
import astra.ui.Ui;

import java.time.LocalDate;
import java.time.LocalTime;

public class AddExamCommand extends AddCommand {
    // private LocalTime startTime;
    // private LocalTime endTime;
    private final String input;

    public AddExamCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
             String[] parts = input.split(" ", 2);
            String args = parts[1];
            String[] details = args.split(" /");
            String description = details[0].trim();
            String dateStr = "", startTimeStr = "", endTimeStr = "";

            for (String detail : details) {
                if (detail.startsWith("date ")) {
                    dateStr = detail.substring(5).trim();
                } else if (detail.startsWith("from ")) {
                    startTimeStr = detail.substring(5).trim();
                } else if (detail.startsWith("to ")) {
                    endTimeStr = detail.substring(3).trim();
                }
            }

            LocalDate date = LocalDate.parse(dateStr);
            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);

            // If you want to support venue, parse it here. Otherwise, pass "".
            Exam exam = new Exam(description, "", date, startTime, endTime);
            activities.addActivity(exam);
            ui.showMessage(exam.toString());

        } catch (Exception e) {
            ui.showError("Invalid exam command format.");
        }
        return false;
    }
}
