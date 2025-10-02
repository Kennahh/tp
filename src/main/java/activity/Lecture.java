package activity;

import java.time.LocalTime;

public class Lecture extends SchoolActivity {
    private String Day;


    public Lecture(String venue, String Day, LocalTime startTime, LocalTime endTime) {
        this.venue = venue;
        this.Day = Day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
