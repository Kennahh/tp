package astra;

import astra.data.Notebook;
import astra.ui.Ui;
import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.parser.Parser;
import astra.command.Command;
import astra.exception.InputException;
import astra.exception.FileSystemException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Astra {
    /**
     * User interface handler for displaying messages and interactions
     */
    private final Ui ui;

    /**
     * Scanner for reading user input from console
     */
    private final Scanner scanner;

    /**
     * Storage handler for saving and loading of activithelpies
     */
    private final Notebook notebook;

    /**
     * Stores user activities during runtime
     */
    private final ActivityList activities;

    /**
     * Initializes the Astra application with necessary components.
     */
    public Astra(String filePath) {
        this.ui = new Ui();
        this.scanner = new Scanner(System.in);
        this.notebook = new Notebook(filePath);
        this.activities = new ActivityList(); // relook into whether can just pass `notebook` in
        try {
            List<Activity> loaded = notebook.loadFromFile();
            for (Activity activity : loaded) {
                this.activities.addActivity(activity);
            }
        } catch (FileSystemException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Runs the interactive command loop.
     */
    public void run() {
        ui.showLogo();
        ui.showBotIntro();
        LocalDate today = LocalDate.now();
        activities.listAndDeleteOverdueTasks(today);
        activities.deadlineReminder(today);
        ui.showPrompt();

        while (true) {
            try {
                ui.showDash();
                String input = scanner.nextLine();
                ui.showDash();

                Command command = Parser.parse(input);
                boolean shouldExit = command.execute(activities, ui, notebook);
                if (!shouldExit) {
                    notebook.writeToFile(activities.toList());
                    notebook.saveToFile(activities);
                }
                if (shouldExit) {
                    ui.showEnd();
                    break;
                }
            } catch (InputException | FileSystemException | IOException e) {
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
