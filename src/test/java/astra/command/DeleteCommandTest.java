package astra.command;

import astra.activity.ActivityList;
import astra.activity.Lecture;
import astra.data.Notebook;
import astra.testutil.TestUi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCommandTest {
    @TempDir
    Path temp;

    private Notebook nb() {
        return new Notebook(temp.resolve("data.txt").toString());
    }

    @Test
    public void deleteCommand_noTaskNumberInput_exception() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new DeleteCommand("delete").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains("Task number can't be empty!")));
    }

    @Test
    public void deleteCommand_indexOutOfBound_exception() {
        ActivityList list = new ActivityList();
        list.addActivity(new Lecture("L1", "LT", DayOfWeek.MONDAY, LocalTime.parse("10:00"),
                LocalTime.parse("12:00")));
        TestUi ui = new TestUi();
        new DeleteCommand("delete 2").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s ->
                s.contains("Activity of matching index does not exist/No index provided!")));
    }

    @Test
    public void deleteCommand_indexFormatError_exception() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new DeleteCommand("delete a").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains("Index provided is not a number!")));
    }

    @Test
    public void deleteCommand_deleteOneTask_success() {
        ActivityList list = new ActivityList();
        list.addActivity(new Lecture("CS2113", "LT9", DayOfWeek.FRIDAY, LocalTime.parse("16:00"),
                LocalTime.parse("18:00")));
        TestUi ui = new TestUi();
        new DeleteCommand("delete 1").execute(list, ui, nb());
        assertTrue(ui.messages.stream().anyMatch(s -> s.contains("Erased: #1 " +
                "CS2113 | Venue: LT9 | Friday | Duration: 1600H to 1800H")));
    }
}