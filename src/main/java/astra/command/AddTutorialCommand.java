package astra.command;

import astra.activity.ActivityList;
import astra.activity.Tutorial;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.ui.Ui;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class AddTutorialCommand extends AddCommand {
    private final String input;

    public AddTutorialCommand(String input){
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] parts = input.split(" ", 2);
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new InputException("Missing tutorial description and details. Use: tutorial <description> /place <venue> /day <day> /from <HH:MM> /to <HH:MM>");
            }
            String args = parts[1].trim();

            String[] details = args.split("/");
            String description = details[0].trim();
            if (description.isEmpty()) {
                throw new InputException("Tutorial description is empty!!!");
            }

            String venue = ""; 
            String day = ""; 
            String startTimeStr = ""; 
            String endTimeStr = "";

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

            if (venue.isEmpty()) {
                throw new InputException("Missing venue. Use: /place <venue>");
            }
            if (day.isEmpty()) {
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
                startTime = LocalTime.parse(startTimeStr);
            } catch (DateTimeParseException e) {
                throw new InputException("Invalid start time format. Use HH:MM.");
            }

            try {
                endTime = LocalTime.parse(endTimeStr);
            } catch (DateTimeParseException e) {
                throw new InputException("Invalid end time format. Use HH:MM.");
            }
            
            if (!endTime.isAfter(startTime)) {
                throw new InputException("End time must be after start time.");
            }

            Tutorial tutorial = new Tutorial(description, venue, day, startTime, endTime);
            activities.addActivity(tutorial);
            ui.showMessage(tutorial.toString());
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
