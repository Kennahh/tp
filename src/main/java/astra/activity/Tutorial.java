package astra.activity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Tutorial extends SchoolActivity{
    private DayOfWeek day;

    public Tutorial(String description, String venue, DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        super(description);
        this.venue = venue;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDayString() {
        String dayString = day.toString().substring(0,1)
                + day.toString().substring(1).toLowerCase();
        return dayString;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public String toString() {
        return super.toString()
                + " | Venue: "
                + venue
                + " | "
                + getDayString()
                + " | Duration: "
                + startTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH))
                + "H to "
                + endTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH))
                + "H";
    }

    @Override
    public String writeToFile() {
        return "Tutorial, "
                + description + ", "
                + venue + ", "
                + day + ", "
                + startTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH)) + ", "
                + endTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH));
    }
}
