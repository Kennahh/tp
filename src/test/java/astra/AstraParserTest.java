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


public class AstraParserTest {

    /** User interface handler for displaying messages and interactions */
    private Ui ui;

    /** Storage handler for saving and loading of activithelpies */
    private Notebook notebook;

    /** Stores user activities during runtime */
    private ActivityList activities;

    @Test
    public void testAddTask_validInput_expectSuccess() {
        this.ui = new Ui();
        this.activities = new ActivityList();
        this.notebook = new Notebook("test.txt");
        String inputString = "task test /by 2025-12-12 14:00";
        try {
            Command command = Parser.parse(inputString);
            assertEquals(false, command.execute(activities, ui, notebook));
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
    }
}
