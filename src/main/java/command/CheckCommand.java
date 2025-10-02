package command;

public abstract class CheckCommand implements Command {
    private String type;
    //private ActivityList activities;

    @Override
    public abstract void execute();

    @Override
    public boolean isExit() {
        return false;
    }
}
