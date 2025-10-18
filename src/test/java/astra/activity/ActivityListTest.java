package astra.activity;

import astra.ui.Ui;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.ByteArrayOutputStream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityListTest {

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
        Ui ui = new Ui();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        list.deadlineReminder();
        String output = outContent.toString();
        String expectedOutput_1 = ("These tasks are due soon. Reminder to complete them!");
        String expectedOutput_2 = ("No task due for the next 3 days");
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput_1));
        assertTrue(output.contains(expectedOutput_2));
    }
}
