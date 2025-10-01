package astra;

import astra.ui.Ui;
import astra.activity.*;
import astra.parser.Parser;
import astra.command.Command;
import astra.exception.InputException;
import astra.exception.FileSystemException;
import astra.data.Notebook;

import java.util.Scanner;

/**
 * Main class for ASTRA - Academic-Scheduler-Tracker-Reminder-Assistant.
 * Handles the main program loop and coordinates between UI, storage, and commands.
 *
 * It is a digital notebook through a command-line interface for users to manage their academic workload.
 * It supports creating, updating, deleting, and viewing different types of academic tasks such as assignments,
 * lectures, tutorials, and exams.
 * 
 * @author Kurokishi592, Ekko-Technology, BTslayer761, Kennahh, JeanPerrierIII
 * @version 1.0
 * @since yyyy-MM-dd
 */
public class Astra {
    /** User interface handler for displaying messages and interactions */
    private final Ui ui;

    /** Storage handler for saving and loading tasks from file */
    //private final Notebook notebook;

    /** Task list manager containing all user tasks */
    //private final ActivityList activities;

    /** Scanner for reading user input from console */
    private final Scanner scanner;

    /**
     * Constructs a new ASTRA chatbot instance.
     * Initializes the UI, storage system, task list, and input scanner.
     * 
     * @param filePath Path to the data file for task storage
     */
    public Astra(String filePath) {
        this.ui = new Ui();
        //this.notebook = new Notebook(filePath);
        //this.activity = new TaskList(storage);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Runs the interactive command loop.
     */
    public void run() {
        //ui.showLogo();
        //ui.showBotIntro();

        boolean isRunning = true;
        while (isRunning) {
            try {
                //ui.showPrompt();
                String input = scanner.nextLine();

                //Command command = Parser.parse(input);
                //boolean shouldExit = command.execute(tasks, ui);

                if (false) {
                    //ui.showEnd();
                    isRunning = false;
                    break;
                } 
//                if (!input.trim().equalsIgnoreCase("list")) {
//                    tasks.saveTasks();
//                }
            } catch (InputException | FileSystemException e) {
                //ui.showError(e.getMessage());
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