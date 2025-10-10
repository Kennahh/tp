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
    private static final String PROMPT_COMMAND = ("[ASTRA] Ready to help you stay on top of your academic game!\n" +
            ">>     Type in a command for me to fulfill your wish!\n" +
            ">>     Type 'help' to see the list of wishes I can fulfill!\n" +
            ">>     Type 'close' to close this digital notebook of yours.\n" +
            "[ASTRA] Monitoring your deepest wishes..."
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
                    ASTRA - Notebook Possibilities!
                ======================================
                General Notes:
                - Commands are case-insensitive for the first word.
                - Use spaces between arguments and prefixes.
                - Time format: HHmm (e.g., 1400 for 2 PM).
                - Date format: YYYY-MM-DD (e.g., 2025-12-10 for 10th December 2025).

                Adding entries to Astra:
                - task <description> /by <YYYY-MM-DD> <HH:MM>
                - lecture <description> /place <venue> /day <day> /from <HH:MM> /to <HH:MM>
                - tutorial <description> /place <venue> /day <day> /from <HH:MM> /to <HH:MM>
                - exam <description> /date <YYYY-MM-DD> /from <HH:MM> /to <HH:MM>
                    
                    Example: task CS2113 Quiz /by 2025-10-10 23:59
                             lecture CS2113 /place LT9 /day Friday /from 16:00 /to 18:00
                             tutorial CS2113 T1 /place COM2-0207 /day Wednesday /from 12:00 /to 13:00
                             exam CS2107 Midterm /date 2025-10-10 /from 10:00 /to 12:00

                Listing and Checking Tasks:
                - checkcurrent                     (Shows the immediate upcoming task)
                - listtask [-deadline] [-priority] (lists all tasks only, [by nearest deadline] or [by priority])
                - checkexam                        (lists all upcoming exams with date and duration) 
                - checklecture <day>               (lists all lectures on a specific day)
                - checktutorial <day>              (lists all tutorials on a specific day)  

                Editing entries in Astra:
                - delete <index>
                - complete <index>                                      (mark as complete)
                - unmark <index>                                        (mark as incomplete)
                - changedeadline <task index> /to <YYYY-MM-DD> <HH:MM>
                    Example: changedeadline 1 /to 2025-10-31 14:00

                Setting Task Priority
                - setpriority <index> as <priority>
                    Example: setpriority 1 as 2

                Help/Exit:
                - help
                - close
                """;
        System.out.println(helpMessage);
    }
}
