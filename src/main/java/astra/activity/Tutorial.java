package astra.activity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Tutorial extends SchoolActivity{
    private String day;

    public Tutorial(String description, String venue, String day, LocalTime startTime, LocalTime endTime) {
        super(description);
        this.venue = venue;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String toString() {
        return super.toString()
                + " | "
                + day
                + " | Duration: "
                + startTime.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H to "
                + endTime.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H";
    }
}
