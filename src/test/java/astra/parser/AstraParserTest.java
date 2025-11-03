package astra.parser;

import astra.activity.ActivityList;
import astra.activity.Exam;
import astra.activity.Task;
import astra.activity.Tutorial;
import astra.command.Command;
import astra.data.Notebook;
import astra.exception.InputException;
import astra.ui.Ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AstraParserTest {

    private Ui ui; // User interface handler for displaying messages and interactions
    private Notebook notebook; // data files for loading and storing of user activities
    private ActivityList activities; // runtime-storage for user activities

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream(); // to capture system out
    private PrintStream originalOut; // store real console output

    @BeforeEach
    void setup() {
        this.ui = new Ui();
        this.activities = new ActivityList();
        this.notebook = new Notebook(Path.of("build", "test-data", "parser-test.txt").toString());
        originalOut = System.out;
        outContent.reset();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut); // restore normal console output
    }

    @Test
    public void testAddTask_validInput_expectSuccess() {
        setup();
        String inputString = "task test /by 2025-12-12 14:00 /priority 1";
        try {
            Command command = Parser.parse(inputString);
            assertEquals(false, command.execute(activities, ui, notebook)); // this is not a good method of testing
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
        tearDown();
    }

    @Test
    public void addTask_valid_success() throws Exception {
        Command c = Parser.parse("task test /by 2025-12-12 14:00 /priority 1");
        c.execute(activities, ui, notebook);
        assertEquals(1, activities.getListSize());
        assertTrue(activities.getActivity(0) instanceof Task);
    }

    @Test
    public void addExam_valid_success() throws Exception {
        Command c = Parser.parse("exam test /place MPSH5 /date 2025-12-12 /from 12:00 /to 18:00");
        c.execute(activities, ui, notebook);
        assertEquals(1, activities.getListSize());
        assertTrue(activities.getActivity(0) instanceof Exam);
    }
    
    @Test
    public void addTutorial_valid_success() throws Exception {
        Command c = Parser.parse("tutorial test /place ERC /day mon /from 13:00 /to 14:00");
        c.execute(activities, ui, notebook);
        assertEquals(1, activities.getListSize());
        assertTrue(activities.getActivity(0) instanceof Tutorial);
    }

    @Test
    public void complete_valid_marksComplete() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00 /priority 1").execute(activities, ui, notebook);
        Task t = (Task) activities.getActivity(0);
        assertFalse(t.getIsComplete());
        Parser.parse("complete 1").execute(activities, ui, notebook);
        assertTrue(t.getIsComplete());
    }

    @Test
    public void addTask_missingPriority_failsAndPrintsError() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("Missing '/priority'"));
    }

    @Test
    public void addTask_invalidPriorityNonInteger_errorShown() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00 /priority xyz").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("Priority must be a valid integer"));
    }

    @Test
    public void addTask_invalidPriorityNegative_errorShown() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00 /priority -2").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("Priority must be a positive integer"));
    }

    @Test
    public void complete_missingIndex_showsError() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00 /priority 1").execute(activities, ui, notebook);
        Parser.parse("complete").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("Please provide an index: complete <index>"));
    }

    @Test
    public void completeTwice_sameIndex_errorShown() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00 /priority 1").execute(activities, ui, notebook);
        Parser.parse("complete 1").execute(activities, ui, notebook);
        Parser.parse("complete 1").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("is already completed"));
    }

    @Test
    public void complete_onTutorial_errorShown() throws Exception {
        Parser.parse("tutorial cs2113 /place com1 /day Mon /from 10:00 /to 11:00").execute(activities, ui, notebook);
        Parser.parse("complete 1").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("is not a Task"));
    }

    @Test
    public void unmark_valid_togglesOff() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00 /priority 1").execute(activities, ui, notebook);
        Parser.parse("complete 1").execute(activities, ui, notebook);
        Parser.parse("unmark 1").execute(activities, ui, notebook);
        Task t = (Task) activities.getActivity(0);
        assertFalse(t.getIsComplete());
    }
    @Test
    public void unmark_missingIndex_errorShown() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00 /priority 1").execute(activities, ui, notebook);
        Parser.parse("unmark").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("Please provide an index: unmark <index>"));
    }

    @Test
    public void unmark_alreadyUnmarked_showsError() throws Exception {
        Parser.parse("task t /by 2025-12-12 14:00 /priority 1").execute(activities, ui, notebook);
        Parser.parse("unmark 1").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("Activity at index 1 is already unmarked"));
    }

    @Test
    public void unmark_onTutorial_errorShown() throws Exception {
        Parser.parse("tutorial cs2113 /place com1 /day Mon /from 10:00 /to 11:00").execute(activities, ui, notebook);
        Parser.parse("unmark 1").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("is not a Task"));
    }

    @Test
    public void complete_onLecture_errorShown() throws Exception {
        Parser.parse("lecture cs2113 /place com1 /day Mon /from 10:00 /to 11:00").execute(activities, ui, notebook);
        Parser.parse("complete 1").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("is not a Task"));
    }

    @Test
    public void complete_onExam_errorShown() throws Exception {
        Parser.parse("exam CS2107 Midterm /place MPSH1 " +
                "/date 2025-10-10 /from 10:00 /to 12:00").execute(activities, ui, notebook);
        Parser.parse("complete 1").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("is not a Task"));
    }

    @Test
    public void unmark_onExam_errorShown() throws Exception {
        Parser.parse("exam CS2107 Midterm /place MPSH1 " +
                "/date 2025-10-10 /from 10:00 /to 12:00").execute(activities, ui, notebook);
        Parser.parse("unmark 1").execute(activities, ui, notebook);
        String output = outContent.toString();
        assertTrue(output.contains("is not a Task"));
    }


}
