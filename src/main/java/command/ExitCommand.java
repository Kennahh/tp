package command;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        //Exit Ui
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
