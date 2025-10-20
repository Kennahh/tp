package astra.activity;


import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Task extends Activity {
    private LocalDate deadlineDate;
    private LocalTime deadlineTime;
    private boolean isComplete = false;
    private int priority = 1;

    public Task(String description, LocalDate deadlineDate, LocalTime deadlineTime, int priority) {
        super(description);
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
        this.priority = priority;
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

    public int getPriority() {
        return priority;
    } 

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "["
            + (isComplete ? "X" : " ")
            + "]" + description
            + " | Deadline: "
            + deadlineDate.format(DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)) // No year!
            + ", "
            + deadlineTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH)) + "H"
            + " | Priority: " + priority;
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
                + deadlineDate.format(DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)) + ", "
                + deadlineTime.format(DateTimeFormatter.ofPattern("HHmm", Locale.ENGLISH)) + ", "
                + priority;
    }
}
