package astra.command;

import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.testutil.TestUi;
import astra.command.AddLectureCommand;
import astra.command.AddTaskCommand;
import astra.command.ChangeDeadlineCommand;
import astra.command.ChangePriorityCommand;
import astra.command.CompleteCommand;
import astra.command.DeleteCommand;
import astra.command.UnmarkCommand;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UpdateCommandsTest {

    @TempDir Path temp;

    private Notebook nb() {
        return new Notebook(temp.resolve("data.txt").toString());
    }

    @Test
    public void completeAndUnmark_toggleFlags_toggledStatus() {
        ActivityList list = new ActivityList();
        list.addActivity(new Task("A", LocalDate.parse("2025-10-10"), LocalTime.parse("23:59"), 1));
        TestUi ui = new TestUi();

        new CompleteCommand("complete 1").execute(list, ui, nb());
        assertTrue(((Task) list.getActivity(0)).getIsComplete());

        new UnmarkCommand("unmark 1").execute(list, ui, nb());
        assertFalse(((Task) list.getActivity(0)).getIsComplete());
    }

    @Test
    public void delete_valid_removesItem() {
        ActivityList list = new ActivityList();
        list.addActivity(new Task("C", LocalDate.parse("2025-10-10"), LocalTime.parse("12:00"), 2));
        list.addActivity(new Task("D", LocalDate.parse("2025-10-11"), LocalTime.parse("13:00"), 3));
        TestUi ui = new TestUi();

        new DeleteCommand("delete 1").execute(list, ui, nb());
        assertEquals(1, list.getListSize());
        assertEquals("D", ((Task) list.getActivity(0)).getDescription());
    }

    @Test
    public void changeDeadline_valid_updatesDateTime() {
        ActivityList list = new ActivityList();
        list.addActivity(new Task("B", LocalDate.parse("2025-10-10"), LocalTime.parse("12:00"), 4));
        TestUi ui = new TestUi();

        new ChangeDeadlineCommand("changedeadline 1 /to 2025-12-31 18:45").execute(list, ui, nb());
        Task t = (Task) list.getActivity(0);
        assertEquals(LocalDate.parse("2025-12-31"), t.getDeadlineDate());
        assertEquals(LocalTime.parse("18:45"), t.getDeadlineTime());
    }

    @Test
    public void changePriority_valid_success() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 21:59 /priority 1").execute(list, ui, nb());
        new AddTaskCommand("task B /by 2025-11-11 22:59 /priority 2").execute(list, ui, nb());
        new AddTaskCommand("task C /by 2025-12-12 23:59 /priority 3").execute(list, ui, nb());
        new ChangePriorityCommand("changepriority 1 /to 2").execute(list, ui, nb());
        new ChangePriorityCommand("changepriority 3 /to 2").execute(list, ui, nb());
        Task t1 = (Task) list.getActivity(0);
        assertEquals(3, t1.getPriority());
        Task t2 = (Task) list.getActivity(1);
        assertEquals(1, t2.getPriority());
        Task t3 = (Task) list.getActivity(2);
        assertEquals(2, t3.getPriority());
    }

    @Test
    public void changePriority_invalidPriority_failure() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 23:59 /priority 1").execute(list, ui, nb());
        try {
            new ChangePriorityCommand("changepriority 1 /to -1").execute(list, ui, nb());
            fail("New priority should always be positive.");
        } catch (AssertionError e) {
            assertEquals("New priority should always be positive.", e.getMessage());
        }
    }

    @Test
    public void changePriority_invalidIndex_failure() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 23:59 /priority 1").execute(list, ui, nb());
        try {
            new ChangePriorityCommand("changepriority -1 /to 1").execute(list, ui, nb());
            fail("Task number should always be positive.");
        } catch (AssertionError e) {
            assertEquals("Task number should always be positive.", e.getMessage());
        }
    }

    @Test
    public void changePriority_missingToFlag_exception() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 23:59 /priority 1").execute(list, ui, nb());
        new ChangePriorityCommand("changepriority 1").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains(
                "Missing '/to' keyword. Use: changepriority <task number> /to <priority>")));
    }

    @Test
    public void changePriority_invalidTaskNumber_error() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 23:59 /priority 1").execute(list, ui, nb());
        new ChangePriorityCommand("changepriority A /to 1").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains(
                "Task number must be an integer.")));
    }

    @Test
    public void changePriority_invalidPriorityNumber_error() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 23:59 /priority 1").execute(list, ui, nb());
        new ChangePriorityCommand("changepriority 1 /to A").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains(
                "New priority must be an integer.")));
    }

    @Test
    public void changePriority_outOfRange_error() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 23:59 /priority 1").execute(list, ui, nb());
        new ChangePriorityCommand("changepriority 2 /to 1").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains(
                "Task number out of range.")));
    }

    @Test
    public void changePriority_samePriority_error() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 23:59 /priority 1").execute(list, ui, nb());
        new AddTaskCommand("task B /by 2025-11-11 22:59 /priority 2").execute(list, ui, nb());
        new ChangePriorityCommand("changepriority 1 /to 1").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains(
                "The task already has this priority.")));
    }

    @Test
    public void changePriority_nonTaskActivity_error() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddLectureCommand("lecture A /place LT9 /day fri /from 1600 /to 1800").execute(list, ui, nb());
        new AddTaskCommand("task B /by 2025-10-10 23:59 /priority 1").execute(list, ui, nb());
        new ChangePriorityCommand("changepriority 1 /to 1").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains(
                "The selected activity is not a task.")));
    }

    @Test
    public void changePriority_validShiftUpwards_updatesAllPriorities() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 20:00 /priority 1").execute(list, ui, nb());
        new AddTaskCommand("task B /by 2025-10-11 21:00 /priority 2").execute(list, ui, nb());
        new AddTaskCommand("task C /by 2025-10-12 22:00 /priority 3").execute(list, ui, nb());

        // Move task 3 to priority 1
        new ChangePriorityCommand("changepriority 3 /to 1").execute(list, ui, nb());

        Task t1 = (Task) list.getActivity(0);
        Task t2 = (Task) list.getActivity(1);
        Task t3 = (Task) list.getActivity(2);

        assertEquals(2, t1.getPriority(), "A should now be priority 2");
        assertEquals(3, t2.getPriority(), "B should now be priority 3");
        assertEquals(1, t3.getPriority(), "C should now be priority 1");
    }

    @Test
    public void changePriority_validShiftDownwards_updatesAllPriorities() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task X /by 2025-10-10 12:00 /priority 1").execute(list, ui, nb());
        new AddTaskCommand("task Y /by 2025-10-11 13:00 /priority 2").execute(list, ui, nb());
        new AddTaskCommand("task Z /by 2025-10-12 14:00 /priority 3").execute(list, ui, nb());

        // Move task 1 to priority 3
        new ChangePriorityCommand("changepriority 1 /to 3").execute(list, ui, nb());

        Task t1 = (Task) list.getActivity(0);
        Task t2 = (Task) list.getActivity(1);
        Task t3 = (Task) list.getActivity(2);

        assertEquals(3, t1.getPriority());
        assertEquals(1, t2.getPriority());
        assertEquals(2, t3.getPriority());
    }

    @Test
    public void changePriority_priorityOutOfUpperRange_error() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task Alpha /by 2025-10-10 20:00 /priority 1").execute(list, ui, nb());
        new AddTaskCommand("task Beta /by 2025-10-11 20:00 /priority 2").execute(list, ui, nb());

        new ChangePriorityCommand("changepriority 1 /to 5").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains("Priority must be between 1 and 2.")));
    }

    @Test
    public void changePriority_invalidFormat_missingSpace_error() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 20:00 /priority 1").execute(list, ui, nb());

        new ChangePriorityCommand("changepriority1/to2").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains("Argument missing")));
    }

    @Test
    public void deleteTask_updatesRemainingTaskPrioritiesCorrectly() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task A /by 2025-10-10 20:00 /priority 1").execute(list, ui, nb());
        new AddTaskCommand("task B /by 2025-10-11 21:00 /priority 2").execute(list, ui, nb());
        new AddTaskCommand("task C /by 2025-10-12 22:00 /priority 3").execute(list, ui, nb());

        // Delete the middle task (priority 2)
        new DeleteCommand("delete 2").execute(list, ui, nb());

        assertEquals(2, list.getListSize(), "List should contain 2 tasks after deletion");

        Task first = (Task) list.getActivity(0);
        Task second = (Task) list.getActivity(1);

        // Ensure priorities are renumbered correctly
        assertEquals(1, first.getPriority(), "First remaining task should have priority 1");
        assertEquals(2, second.getPriority(), "Second remaining task should have priority 2");

        // Ensure task descriptions correspond correctly
        assertEquals("A", first.getDescription());
        assertEquals("C", second.getDescription());
    }
}
