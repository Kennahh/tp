package astra.activity;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task extends Activity {
    private LocalDateTime deadline_date_time;
    private boolean isComplete = false;

    public Task(String description, LocalDateTime deadline) {
        this.description = description;
        this.deadline_date_time = deadline;
    }

    @Override
    public String toString() {
        return "["
                + (isComplete ? "X" : " ")
                + "] "
                + description
                + " | Deadline: "
                + deadline_date_time.format(DateTimeFormatter.ofPattern("d MMM"))
                + ", "
                + deadline_date_time.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H";
    }
}
