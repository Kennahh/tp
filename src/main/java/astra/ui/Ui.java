package astra.ui;

/*
 * Handles user interface output.
 */
public class Ui {
    private static final String DASH_LINE ="------------------------------------------------------------";
    private static final String NAME = "Astra";
    private static final String BOT_INTRO = ("[ASTRA] Hello! I am " + NAME +
            "! Your friendly digital academic notebook :)\n" +
            "I am here to support your academic universe!\n"
    );
    private static final String PROMPT_COMMAND = ("[ASTRA] Monitoring your deepest wishes..." +
            "Speak and you shall receive!\n" +
            ">>     Type 'close' to close this digital notebook of yours.\n" +
            "[ASTRA] Ready to help you stay on top of your academic game!\n"
    );
    private static final String DONE_COMMAND = "[ASTRA] Done! Now, what's your next wish...\n";
    private static final String END_COMMAND = "[ASTRA] Keep up the great work! Your academic triumph awaits!";
    private static final String ASTRA_LOGO = """
                          __________________________________________
                         /\\         _____ _______ _____        /\\
                        /  \\      / ____|__   __|  __  \\      /  \\
                       / /\\ \\    || (___   | |  | |__) |     / /\\ \\
                      / /  \\ \\   \\ ___  \\  | |  |  _  /     / /  \\ \\
                     /_/____\\_\\   ____) |  | |  | |  \\ \\   /_/____\\_\\
                    /_/______\\_\\ |_____/   |_|  |_|   \\_\\ /_/______\\_\\
            """;

    /**
     * Displays a horizontal dash line.
     */
    public void showDash() {
        System.out.println(DASH_LINE);
    }

    /**
     * Displays the Astra logo.
     */
    public void showLogo() {
        System.out.println(ASTRA_LOGO);
    }

    /**
     * Displays the bot introduction message.
     */
    public void showBotIntro() {
        System.out.println(BOT_INTRO);
    }

    /**
     * Displays the command prompt message.
     */
    public void showPrompt() {
        System.out.println(PROMPT_COMMAND);
    }

    /**
     * Displays a general message to the user.
     *
     * @param msg Message to display.
     */
    public void showMessage(String msg) {
        System.out.println(msg);
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg Error message to display.
     */
    public void showError(String msg) {
        System.out.println(msg);
    }

    /**
     * Displays the done command message.
     */
    public void showDone() {
        showDash();
        System.out.println(DONE_COMMAND);
    }

    /**
     * Displays the end command message.
     */
    public void showEnd() {
        System.out.println(END_COMMAND);
        showDash();
    }

    /**
     * Displays list of commands available to the user.
     */
    public void showHelp() {
        String helpMessage = """
                ======================================
                    ASTRA - Command Help
                ======================================
                General Notes:
                - Commands are case-insensitive for the first word.
                - Use spaces between arguments and prefixes.
                - Time format: HHmm (e.g., 1400 for 2 PM).
                - Date format: DD MMM (e.g., 18 Sep).

                Available Commands:

                1. Add/Create Task
                create <description> /by <Date> <DateTime>
                Example: create CS2113 Quiz /by 2025-10-10 23:59

                2. Add Lecture
                lecture <description> /place <venue> /day <day> /from <start_time> /to <end_time>
                Example: lecture CS2113 /place LT9 /day Friday /from 16:00 /to 18:00

                3. Add Tutorial
                tutorial <description> /place <venue> /day <day> /from <start_time> /to <end_time>
                Example: tutorial CS2113 T1 /place COM2-0207 /day Wednesday /from 12:00 /to 13:00

                4. Add Exam
                exam <description> /date <date> /from <start_time> /to <end_time>
                Example: exam CS2107 Midterm /date 2025-10-10 /from 10:00 /to 12:00

                5. List Tasks
                listtask
                listtask -deadline   (sort by nearest deadline)
                listtask -priority   (sort by priority)

                6. Delete Task
                delete <index>
                Example: delete 2

                7. Mark/Unmark Task
                complete <index>     (mark as complete)
                unmark <index>       (mark as incomplete)

                8. Change Deadline
                changedeadline <task index> /to <YYYY-MM-DD> <HH:MM>
                Example: changedeadline 1 /to 2025-10-31 14:00

                9. Check Examinations
                checkexam
                Lists all upcoming exams with date and duration.

                10. Check Lectures
                    checklecture <day>
                    Example: checklecture Friday

                11. Check Tutorials
                    checktutorial <day>
                    Example: checktutorial Wednesday

                12. Check Next Task
                    checkcurrent
                    Shows the immediate upcoming task.

                13. Set Task Priority
                    setpriority <index> as <priority>
                    Example: setpriority 1 as 2

                14. Display help menu
                    help

                15. Exit application
                    close

                ======================================
                """;
        System.out.println(helpMessage);
    }
}
