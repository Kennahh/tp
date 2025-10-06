package astra.activity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Lecture extends SchoolActivity {
    private String Day;


    public Lecture(String description, String venue, String Day, LocalTime startTime, LocalTime endTime) {
        super(description);
        this.venue = venue;
        this.Day = Day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDay() {
        return Day;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | "
                + Day
                + " | Duration: "
                + startTime.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H to "
                + endTime.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H";
    }
}
