package astra.command;

import astra.activity.ActivityList;
import astra.activity.Task;
import astra.data.Notebook;
import astra.testutil.TestUi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
