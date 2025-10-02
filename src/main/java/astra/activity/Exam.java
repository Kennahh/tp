package astra.activity;

import java.time.LocalTime;
import java.time.LocalDate;

public class Exam extends SchoolActivity {
    private LocalDate date;

    public Exam(String venue, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.venue = venue;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

    }
}
