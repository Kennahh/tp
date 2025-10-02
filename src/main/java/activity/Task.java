package activity;

import java.time.LocalTime;
import java.time.LocalDate;

public class Task extends Activity {
    private LocalDate deadline_date;
    private LocalTime deadline_time;

    public Task(String description, LocalDate deadline_date, LocalTime deadline_time) {
        this.description = description;
        this.deadline_date = deadline_date;
        this.deadline_time = deadline_time;
    }
}
