package astra.activity;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Exam extends SchoolActivity {
    private LocalDate date;

    public Exam(String description, String venue, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(description);
        this.venue = venue;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Venue: "
                + venue
                + " | Date: "
                + date.format(DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH))
                + " | Duration: "
                + startTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH))
                + "H to "
                + endTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH))
                + "H";
    }

    @Override
    public String writeToFile() {
        return "Exam, "
                + description + ", "
                + venue + ", "
                + date.format(DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)) + ", "
                + startTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH)) + ", "
                + endTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH));
    }
}
