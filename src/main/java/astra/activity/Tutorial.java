package astra.activity;

import java.time.LocalTime;

public class Tutorial extends SchoolActivity{
    private String day;

    public Tutorial(String venue,String day, LocalTime startTime, LocalTime endTime) {
        this.venue = venue;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
