package astra.command;

import astra.activity.*;
import astra.data.Notebook;
import astra.testutil.TestUi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateCommandsTest {

    @TempDir Path temp;

    private Notebook nb() {
        return new Notebook(temp.resolve("data.txt").toString());
    }

    @Test
    public void addTask_valid_adds() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task Read /by 2025-10-10 23:59").execute(list, ui, nb());
        assertEquals(1, list.getListSize());
        assertTrue(list.getActivity(0) instanceof Task);
    }

    @Test
    public void addTask_missingBy_showsError() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTaskCommand("task Read only").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains("Missing '/by'")));
    }

    @Test
    public void addLecture_valid_adds() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddLectureCommand("lecture CS /place LT19 /day Mon /from 10:00 /to 12:00").execute(list, ui, nb());
        assertEquals(1, list.getListSize());
        assertTrue(list.getActivity(0) instanceof Lecture);
    }

    @Test
    public void addTutorial_valid_adds() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddTutorialCommand("tutorial CS /place COM1 /day Fri /from 14:00 /to 15:00").execute(list, ui, nb());
        assertEquals(1, list.getListSize());
        assertTrue(list.getActivity(0) instanceof Tutorial);
    }

    @Test
    public void addExam_valid_adds() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddExamCommand("exam Finals /date 2025-11-25 /from 09:00 /to 11:00").execute(list, ui, nb());
        assertEquals(1, list.getListSize());
        assertTrue(list.getActivity(0) instanceof Exam);
    }

    @Test
    public void addExam_endBeforeStart_showsError() {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        new AddExamCommand("exam Finals /date 2025-11-25 /from 11:00 /to 10:00").execute(list, ui, nb());
        assertTrue(ui.errors.stream().anyMatch(s -> s.contains("End time must be after start time")));
    }
}