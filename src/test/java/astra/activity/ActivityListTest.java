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

        Task t = new Task("Do CS", LocalDate.parse("2025-10-10"), LocalTime.parse("23:59"), 1);
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
        ActivityList list = new ActivityList();
        Ui ui = new Ui();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are due soon. Reminder to complete them!");
        String expectedOutput2 = ("1. Read | Days left: 2");

        new AddTaskCommand("task Read /by 2025-10-20 23:59 /priority 1").execute(list, ui, nb());
        assertTrue(list.getActivity(0) instanceof Task);

        list.deadlineReminder(date);
        String output = outContent.toString();

        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
    }

    @Test
    public void deadlineReminder_singleTutorialInTaskList_success() {
        ActivityList list = new ActivityList();
        Ui ui = new Ui();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are due soon. Reminder to complete them!");
        String expectedOutput2 = ("No task due for the next 3 days");

        new AddTutorialCommand("tutorial CS /place COM1 /day " +
                "Fri /from 14:00 /to 15:00").execute(list, ui, nb());
        assertTrue(list.getActivity(0) instanceof Tutorial);

        list.deadlineReminder(date);
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
    }

    @Test
    public void listAndDeleteOverdueTasks_emptyList_printNoTask() {
        ActivityList list = new ActivityList();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are either overdue or completed and have been removed from the list");
        String expectedOutput2 = ("No overdue or completed tasks have been deleted!");

        list.listAndDeleteOverdueTasks(date);
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
    }

    @Test
    public void listAndDeleteOverdueTasks_singleTaskNotOverdue_printNoTask() {
        ActivityList list = new ActivityList();
        Ui ui = new Ui();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are either overdue or completed and have been removed from the list");
        String expectedOutput2 = ("No overdue or completed tasks have been deleted!");

        new AddTaskCommand("task Read /by 2025-11-10 23:59 /priority 1").execute(list, ui, nb());
        assertTrue(list.getActivity(0) instanceof Task);
        assertTrue(list.getListSize() == 1);

        list.listAndDeleteOverdueTasks(date);
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
        assertTrue(list.getListSize() == 1);
    }

    @Test
    public void listAndDeleteOverDueTasks_twoOverdueTasks_printAndDeleteTwoTask() {
        ActivityList list = new ActivityList();
        Ui ui = new Ui();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are either overdue or completed and have been removed from the list");
        String expectedOutput2 = ("1. Read");
        String expectedOutput3 = ("2. CS2113 assignment");

        new AddTaskCommand("task Read /by 2025-10-10 23:59 /priority 1")
                .execute(list, ui, nb());
        assertTrue(list.getActivity(0) instanceof Task);
        new AddTaskCommand("task CS2113 assignment /by 2024-02-02 /priority 2")
                .execute(list, ui, nb());
        assertTrue(list.getActivity(1) instanceof Task);
        assertTrue(list.getListSize() == 2);

        list.listAndDeleteOverdueTasks(date);
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
        assertTrue(output.contains(expectedOutput3));
        assertTrue(list.getListSize() == 0);
    }

    @Test
    public void listAndDeleteOverDueTasks_oneTutorial_printNoTask() {
        ActivityList list = new ActivityList();
        Ui ui = new Ui();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are either overdue or completed and have been removed from the list");
        String expectedOutput2 = ("No overdue or completed tasks have been deleted!");

        new AddTutorialCommand("tutorial CS /place COM1 /day " +
                "Fri /from 14:00 /to 15:00").execute(list, ui, nb());
        assertTrue(list.getActivity(0) instanceof Tutorial);

        list.listAndDeleteOverdueTasks(date);
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
        assertTrue(list.getListSize() == 1);
    }

    @Test
    public void listAndDeleteOverdueTasks_singleTaskCompleted_printTask() {
        ActivityList list = new ActivityList();
        Ui ui = new Ui();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        LocalDate date = LocalDate.of(2025, 10, 18);
        String expectedOutput1 = ("These tasks are either overdue or completed and have been removed from the list");
        String expectedOutput2 = ("1. Read");

        new AddTaskCommand("task Read /by 2025-11-10 23:59 /priority 1").execute(list, ui, nb());
        assertTrue(list.getActivity(0) instanceof Task);
        assertTrue(list.getListSize() == 1);
        Task currTask = (Task) list.getActivity(0);
        currTask.setIsComplete();
        assertTrue(currTask.getIsComplete());

        list.listAndDeleteOverdueTasks(date);
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
        assertTrue(list.getListSize() == 0);
    }
}

