package astra.activity;

import org.junit.jupiter.api.Test;

// import src.main.java.astra.activity.Task;
import astra.activity.Task;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {

    @Test
    public void completeToggle_deadlineUpdate_andWriteToFile() {
        Task t = new Task("Read", LocalDate.parse("2025-09-01"), LocalTime.parse("20:30"), 2);
        assertFalse(t.getIsComplete());
        assertEquals("0", t.statusInIcon());
        assertEquals(2, t.getPriority());

        t.setIsComplete();
        assertTrue(t.getIsComplete());
        assertEquals("1", t.statusInIcon());

        t.clearIsComplete();
        assertFalse(t.getIsComplete());
        assertEquals("0", t.statusInIcon());

        t.setDeadline(LocalDate.parse("2025-10-01"), LocalTime.parse("21:45"));
        assertEquals(LocalDate.parse("2025-10-01"), t.getDeadlineDate());
        assertEquals(LocalTime.parse("21:45"), t.getDeadlineTime());

        assertTrue(t.toString().contains("Deadline"));
        assertTrue(t.writeToFile().startsWith("Task, "));
    }

    @Test
    public void priority_setAndGet_works() {
        Task t = new Task("PriorityTest", LocalDate.parse("2025-10-10"), LocalTime.parse("12:00"), 5);
        assertEquals(5, t.getPriority());
        t.setPriority(3);
        assertEquals(3, t.getPriority());
    }
}
