package astra;

import astra.data.Notebook;
import astra.ui.Ui;
import astra.activity.*;
import astra.parser.Parser;
import astra.command.Command;
import astra.exception.InputException;

import java.util.Scanner;

public class Astra {
    /** User interface handler for displaying messages and interactions */
    private final Ui ui;

    /** Scanner for reading user input from console */
    private final Scanner scanner;

    /** Storage handler for saving and loading of activities */
    private final Notebook notebook;

    /** Stores user activities during runtime */
    private final ActivityList activities;

    /** Initializes the Astra application with necessary components. */
    public Astra(String filePath) {
        this.ui = new Ui();
        this.scanner = new Scanner(System.in);
        this.notebook = new Notebook();
        this.activities = new ActivityList();
    }

    /**
     * Runs the interactive command loop.
     */
    public void run() {
        ui.showLogo();
        ui.showBotIntro();

        boolean isRunning = true;
        while (isRunning) {
            try {
                ui.showPrompt();
                String input = scanner.nextLine();

                Command command = Parser.parse(input);
                boolean shouldExit = command.execute(activities, ui, notebook);
                if (shouldExit) {
                    ui.showEnd();
                    isRunning = false;
                    break;
                }
            } catch (InputException e) {
                ui.showError(e.getMessage());
            }
        }
        scanner.close();
    }

    /**
     * Entry point of the astra application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Astra astra = new Astra("data/tasks.txt");
        astra.run();
    }
}