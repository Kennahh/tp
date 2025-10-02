package astra.command;

public interface Command {
    /**
     * Executes the command.
     * @return true if the app should exit after this command, false otherwise.
     */
    boolean execute();
}
