package astra.command;

import astra.activity.ActivityList;
import astra.activity.Exam;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.ui.Ui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

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
            if (parts.length != 2) {
                throw new InputException("Missing exam description and Datetime details. Use: exam <description> " +
                        "/date <YYYY-MM-DD> /from <HH:MM> /to <HH:MM>");
            }

            String args = parts[1];
            String[] details = args.split("/");
            String description = details[0].trim();
            if (description.isEmpty()) {
                throw new InputException("Exam description is empty!!!");
            }
            String venue = "";
            String dateStr = "";
            String startTimeStr = "";
            String endTimeStr = "";

            for (String detail : details) {
                if (detail.startsWith("place ")) {
                    venue = detail.substring(6).trim();
                } else if (detail.startsWith("date ")) {
                    dateStr = detail.substring(5).trim();
                } else if (detail.startsWith("from ")) {
                    startTimeStr = detail.substring(5).trim();
                } else if (detail.startsWith("to ")) {
                    endTimeStr = detail.substring(3).trim();
                }
            }

            if (dateStr.isEmpty()) {
                throw new InputException("Missing exam date. Use: /date <YYYY-MM-DD>");
            }
            if (startTimeStr.isEmpty()) {
                throw new InputException("Missing start time. Use: /from <HH:MM>");
            }
            if (endTimeStr.isEmpty()) {
                throw new InputException("Missing end time. Use: /to <HH:MM>");
            }

            LocalDate date;
            LocalTime startTime;
            LocalTime endTime;

            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                throw new InputException("Invalid date format. Use YYYY-MM-DD.");
            }

            try {
                startTime = LocalTime.parse(startTimeStr);
            } catch (DateTimeParseException e) {
                throw new InputException("Invalid start time format. Use HH:MM.");
            }

            try {
                endTime = LocalTime.parse(endTimeStr);
            } catch (DateTimeParseException e) {
                throw new InputException("Invalid end time format. Use HH:MM.");
            }

            // Also test that endTime is after startTime
            if (!endTime.isAfter(startTime)) {
                throw new InputException("End time must be after start time.");
            }

            // If you want to support venue, parse it here. Otherwise, pass "".
            Exam exam = new Exam(description, venue, date, startTime, endTime);
            activities.addActivity(exam);
            ui.showMessage(exam.toString());
            notebook.saveToFile(activities);

        } catch (IOException e) {
            ui.showError(e.getMessage());
        } catch (InputException formatError) {
            ui.showError(formatError.getMessage());
        } catch (Exception e) {
            ui.showError("Invalid exam command format.");
        } 
        return false;
    }
}
