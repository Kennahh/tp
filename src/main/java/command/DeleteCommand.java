package command;

public class DeleteCommand implements Command {
    private int index;
    //private ActivityList activities;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute() {

    }

    public boolean isExit() {
        return false;
    }
}
