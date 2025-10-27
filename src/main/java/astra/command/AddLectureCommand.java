package astra.command;

import astra.activity.ActivityList;
import astra.activity.Lecture;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.parser.DateTimeParser;
import astra.ui.Ui;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class AddLectureCommand extends AddCommand {
    private final String input;

    public AddLectureCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] parts = input.split(" ", 2);
            if (parts.length != 2) {
                throw new InputException("Missing lecture description and details. Use: lecture <description> " +
                        "/place <venue> /day <day> /from <HH:MM> /to <HH:MM>");
            }

            String args = parts[1];
            String[] details = args.split("/");
            String description = details[0].trim();
            if (description.isEmpty()) {
                throw new InputException("Lecture description is missing");
            }

            String venue = ""; 
            DayOfWeek day = null;
            String startTimeStr = ""; 
            String endTimeStr = "";

            for (String detail : details) {
                if (detail.startsWith("place ")) {
                    venue = detail.substring(6).trim();
                } else if (detail.startsWith("day ")) {
                    day = DateTimeParser.dayOfWeekParser(detail.substring(4).trim());
                } else if (detail.startsWith("from ")) {
                    startTimeStr = detail.substring(5).trim();
                } else if (detail.startsWith("to ")) {
                    endTimeStr = detail.substring(3).trim();
                }
            }

            if (venue.isEmpty()) {
                throw new InputException("Missing venue. Use: /place <venue>");
            }
            if (day == null) {
                throw new InputException("Missing day. Use: /day <day>");
            }
            if (startTimeStr.isEmpty()) {
                throw new InputException("Missing start time. Use: /from <HH:MM>");
            }
            if (endTimeStr.isEmpty()) {
                throw new InputException("Missing end time. Use: /to <HH:MM>");
            }
                
            LocalTime startTime;
            LocalTime endTime;

            try {
                startTime = DateTimeParser.parseTime(startTimeStr);
            } catch (InputException e) {
                throw new InputException("Invalid start time format. Use HH:MM (24-hour).");
            }

            try {
                endTime = DateTimeParser.parseTime(endTimeStr);
            } catch (InputException e) {
                throw new InputException("Invalid end time format. Use HH:MM (24-hour).");
            }

            if (!endTime.isAfter(startTime)) {
                throw new InputException("End time must be after start time.");
            }

            Lecture lecture = new Lecture(description, venue, day, startTime, endTime);
            activities.addActivity(lecture);
            ui.showMessage(lecture.toString());
            notebook.saveToFile(activities);
            
        } catch (IOException e) {
            ui.showError(e.getMessage());
        } catch (InputException formatError) {
            ui.showError(formatError.getMessage());
        } catch (Exception e) {
            ui.showError("Invalid lecture command format.");
        } 
        return false;
    }
}
