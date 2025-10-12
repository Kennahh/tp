package astra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import astra.activity.ActivityList;
import astra.command.Command;
import astra.data.Notebook;
import astra.exception.FileSystemException;
import astra.exception.InputException;
import astra.parser.Parser;
import astra.ui.Ui;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class AstraParserTest {

    /**
     * User interface handler for displaying messages and interactions
     */
    private Ui ui;

    /**
     * Storage handler for saving and loading of activithelpies
     */
    private Notebook notebook;

    /**
     * Stores user activities during runtime
     */
    private ActivityList activities;

    /**
     * capture System.out
     */
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /**
     * store real console output
     */
    private final PrintStream originalOut = System.out;

    private void setup() {
        this.ui = new Ui();
        this.activities = new ActivityList();
        this.notebook = new Notebook("test.txt");
        System.setOut(new PrintStream(outContent));
    }

    void tearDown() {
        System.setOut(originalOut); // restore normal console output
    }


    @Test
    public void testAddTask_validInput_expectSuccess() {
        setup();
        String inputString = "task test /by 2025-12-12 14:00";
        try {
            Command command = Parser.parse(inputString);
            assertEquals(false, command.execute(activities, ui, notebook)); // this is not a good method of testing
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
        tearDown();
    }

    @Test
    public void testAddExam_validInput_expectSuccess() {
        setup();
        String inputString = "exam test /date 2025-12-12 /from 12:00 /to 18:00";
        try {
            Command command = Parser.parse(inputString);
            assertEquals(false, command.execute(activities, ui, notebook));
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
        tearDown();
    }

    @Test
    public void testAddTutorial_validInput_expectSuccess() {
        setup();
        String inputString = "tutorial test /place ERC /day mon /from 13:00 /to 14:00";
        try {
            Command command = Parser.parse(inputString);
            assertEquals(false, command.execute(activities, ui, notebook));
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
        tearDown();
    }
}
