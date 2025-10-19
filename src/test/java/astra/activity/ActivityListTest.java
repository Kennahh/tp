package astra.activity;

import astra.command.AddTaskCommand;
import astra.command.AddTutorialCommand;
import astra.data.Notebook;
import astra.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.PrintStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.ByteArrayOutputStream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityListTest {
    @TempDir
    Path temp;

    private Notebook nb() {
        return new Notebook(temp.resolve("data.txt").toString());
    }


    @Test
    public void addGetDelete_basicFlow_success() {
        ActivityList list = new ActivityList();
        assertEquals(0, list.getListSize());

        Task t = new Task("Do CS", LocalDate.parse("2025-10-10"), LocalTime.parse("23:59"));
        list.addActivity(t);
        assertEquals(1, list.getListSize());
        assertSame(t, list.getActivity(0));
        assertSame(t, list.getAnActivity(0));

        list.deleteActivity(0);
        assertEquals(0, list.getListSize());
    }

    @Test
    public void deadlineReminder_emptyList_success() {
        ActivityList list = new ActivityList();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        String expectedOutput1 = ("These tasks are due soon. Reminder to complete them!");
        String expectedOutput2 = ("No task due for the next 3 days");

        LocalDate date = LocalDate.of(2025, 10, 18);
        list.deadlineReminder(date);
        String output = outContent.toString();

        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
    }

    @Test
    public void deadlineReminder_singleTaskDueSoon_success() {
        testSetup result = startSetup();

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are due soon. Reminder to complete them!");
        String expectedOutput2 = ("1. Read | Days left: 2");

        new AddTaskCommand("task Read /by 2025-10-20 23:59").execute(result.list(), result.ui(), nb());
        assertTrue(result.list().getActivity(0) instanceof Task);

        result.list().deadlineReminder(date);
        String output = result.outContent().toString();

        System.setOut(result.originalOut());
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
    }

    @Test
    public void deadlineReminder_singleTutorialInTaskList_success() {
        testSetup result = startSetup();

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are due soon. Reminder to complete them!");
        String expectedOutput2 = ("No task due for the next 3 days");

        new AddTutorialCommand("tutorial CS /place COM1 /day " +
                "Fri /from 14:00 /to 15:00").execute(result.list(), result.ui(), nb());
        assertTrue(result.list().getActivity(0) instanceof Tutorial);

        result.list().deadlineReminder(date);
        String output = result.outContent().toString();
        System.setOut(result.originalOut());
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
    }

    private static testSetup startSetup() {
        ActivityList list = new ActivityList();
        Ui ui = new Ui();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        testSetup result = new testSetup(list, ui, outContent, originalOut);
        return result;
    }

    private record testSetup(ActivityList list, Ui ui, ByteArrayOutputStream outContent, PrintStream originalOut) {
    }

}
