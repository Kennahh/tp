package command;

public abstract class AddCommand implements Command {
    protected String description;
    //protected ActivityList activities;

    @Override
    public abstract void execute();

    @Override
    public boolean isExit() {
        return false;
    }
}
