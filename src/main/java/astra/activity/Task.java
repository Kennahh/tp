package astra.activity;


import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task extends Activity {
    private LocalDate deadlineDate;
    private LocalTime deadlineTime;
    private boolean isComplete = false;

    public Task(String description, LocalDate deadline_date, LocalTime deadline_time) {
        super(description);
        this.deadlineDate = deadline_date;
        this.deadlineTime = deadline_time;
    }
    public boolean getIsComplete(){

        return isComplete;
    }

    public void setIsComplete(){

        isComplete = true;
    }

    public void  clearIsComplete(){

        isComplete = false;
    }

    public void setDeadline(LocalDate newDate, LocalTime newTime) {
        this.deadlineDate = newDate;
        this.deadlineTime = newTime;
    }

    public LocalDate getDeadlineDate() {

        return deadlineDate;
    }

    public LocalTime getDeadlineTime() {

        return deadlineTime;
    }

    @Override
    public String toString() {
        return "["
                + (isComplete ? "X" : " ")
                + "]"
                + description
                + " | Deadline: "
                + deadlineDate.format(DateTimeFormatter.ofPattern("d MMM"))
                + ", "
                + deadlineTime.format(DateTimeFormatter.ofPattern("HHmm"))
                + "H";
    }

    public String statusInIcon() {
        if (isComplete) {
            return "1";
        } else {
            return "0";
        }
    }

    public void setComplete(boolean complete) {

        isComplete = complete;
    }

    @Override
    public String writeToFile() {
        return "Task, "
                + description + ", "
                + deadlineDate.format(DateTimeFormatter.ofPattern("d MMM")) + ", "
                + deadlineTime.format(DateTimeFormatter.ofPattern("HHmm")) + ", "
                + statusInIcon();
    }
}
