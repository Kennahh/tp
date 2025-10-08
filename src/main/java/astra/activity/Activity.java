package astra.activity;

public abstract class Activity {
    protected String description;

    public Activity(String description) {
        this.description = description;
    }

    public String toString() {
        return description;
    }

    public String getDescription() {
        return description;
    }

}
