package astra.activity;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Exam extends SchoolActivity {
    private LocalDate date;

    public Exam(String description, String venue, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(description);
        this.venue = venue;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Date: "
                + date.format(DateTimeFormatter.ofPattern("d MMM"))
                + " | Duration: "
                + startTime.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H to "
                + endTime.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H";
    }
}
