package astra.activity;

import java.time.LocalTime;


public abstract class SchoolActivity extends Activity {
    protected String venue;
    protected LocalTime startTime;
    protected LocalTime endTime;

    public SchoolActivity(String description) {
        super(description);
    }

    public String getVenue() {
        return venue;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
}
