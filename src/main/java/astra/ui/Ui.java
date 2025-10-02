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
}
