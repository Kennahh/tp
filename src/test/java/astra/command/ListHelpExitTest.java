package astra.command;

import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.testutil.TestUi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListHelpExitTest {

    @TempDir Path temp;

    @Test
    public void list_printsToStdout() {
        ActivityList list = new ActivityList();
        list.addActivity(new Task("X", LocalDate.parse("2025-10-10"), LocalTime.parse("23:00")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream prev = System.out;
        System.setOut(new PrintStream(out));
        try {
            new ListCommand().execute(list, new TestUi(), new Notebook(temp.resolve("data.txt").toString()));
        } finally {
            System.setOut(prev);
        }
        assertTrue(out.toString().contains("1. "));
    }

    @Test
    public void help_printsHelp() {
        TestUi ui = new TestUi();
        new HelpCommand().execute(new ActivityList(), ui, new Notebook(temp.resolve("data.txt").toString()));
        assertTrue(ui.messages.contains("HELP"));
    }

    @Test
    public void exit_returnsTrue() {
        boolean shouldExit = new ExitCommand().execute(new ActivityList(), new TestUi(),
                new Notebook(temp.resolve("d.txt").toString()));
        assertTrue(shouldExit);
    }
}
