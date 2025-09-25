package seedu.nusmodsplus;

import java.io.FileNotFoundException;


public class NUSModsPlus {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    private static String DEFAULT_SAVE_PATH = "data/NUSModsPlus.txt";
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    public NUSModsPlus(String filepath) {
        ui = new Ui();
        storage = new Storage(filepath);
        try {
            tasks = new TaskList(storage.loadSave());
            ui.saveFound();
        } catch (FileNotFoundException e) {
            // display loading error
            ui.noSave();
            storage.createSave();
            tasks = new TaskList();
        }
    }

    public void exit() {
        // storage.writeSave(tasks);
        ui.printGoodbye();
        System.exit(0);
    }

    public void run() {
        ui.printWelcome();
        boolean isExit = true; // change to false once below has been implemented
        while (!isExit) {
            try {
                String userInput = Parser.getUserInput();
                Command c = Parser.parseCommand(userInput);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (IllegalArgumentException e) {// this is a placeholder exception
                ui.showError(e.getMessage());
            }
        }
        exit();
    }

    public static void main(String[] args) {
        new NUSModsPlus(DEFAULT_SAVE_PATH).run();
    }
}
