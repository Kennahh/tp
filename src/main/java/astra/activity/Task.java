package astra.activity;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task extends Activity {
    private LocalDate deadline_date;
    private LocalTime deadline_time;
    private boolean isComplete = false;

    public Task(String description, LocalDate deadline_date, LocalTime deadline_time) {
        super(description);
        this.deadline_date = deadline_date;
        this.deadline_time = deadline_time;
    }

    @Override
    public String toString() {
        return "["
                + (isComplete ? "X" : " ")
                + "]"
                + description
                + " | Deadline: "
                + deadline_date.format(DateTimeFormatter.ofPattern("d MMM"))
                + ", "
                + deadline_time.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H";
    }
}
