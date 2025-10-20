package astra.command;

import astra.activity.ActivityList;
import astra.activity.Exam;
import astra.activity.Lecture;
import astra.activity.Task;
import astra.activity.Tutorial;
import astra.data.Notebook;
import astra.testutil.TestUi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckCommandsTest {

    @TempDir Path temp;

    private Notebook nb() {
        return new Notebook(temp.resolve("data.txt").toString());
    }

    @Test
    public void checkExam_runs_noCrash() {
        ActivityList list = new ActivityList();
        list.addActivity(new Exam("X", "MPSH", LocalDate.parse("2025-11-25"),
                LocalTime.parse("09:00"), LocalTime.parse("11:00")));
        list.addActivity(new Task("T", LocalDate.parse("2025-10-10"), LocalTime.parse("23:00"), 1));
        new CheckExamCommand().execute(list, new TestUi(), nb());
        assertEquals(2, list.getListSize());
    }

    @Test
    public void checkExam_noExamInList_noExamMessage() {
        ActivityList list = new ActivityList();
        list.addActivity(new Task("T", LocalDate.parse("2025-10-10"), LocalTime.parse("23:00"), 1));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        String expectedOutput = "No exams in your list!";
        new CheckExamCommand().execute(list, new TestUi(), nb());
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.equals(expectedOutput));
    }

    @Test
    public void checkLectures_dayFilter_messageCount() {
        ActivityList list = new ActivityList();
        list.addActivity(new Lecture("L1", "LT", DayOfWeek.MONDAY, LocalTime.parse("10:00"),
                LocalTime.parse("12:00")));
        list.addActivity(new Lecture("L2", "LT", DayOfWeek.FRIDAY, LocalTime.parse("10:00"),
                LocalTime.parse("12:00")));
        TestUi ui = new TestUi();
        new CheckLecturesCommand("Mon").execute(list, ui, nb());
        assertTrue(ui.messages.stream().anyMatch(s -> s.contains("lecture(s)")));
    }

    @Test
    public void checkTutorials_dayFilter_messageCount() {
        ActivityList list = new ActivityList();
        list.addActivity(new Tutorial("T1", "COM1", DayOfWeek.FRIDAY, LocalTime.parse("14:00"),
                LocalTime.parse("15:00")));
        list.addActivity(new Tutorial("T2", "COM1", DayOfWeek.MONDAY, LocalTime.parse("14:00"),
                LocalTime.parse("15:00")));
        TestUi ui = new TestUi();
        new CheckTutorialsCommand("Fri").execute(list, ui, nb());
        assertTrue(ui.messages.stream().anyMatch(s -> s.contains("tutorial(s)")));
    }

    @Test
    public void checkCurrent_showsTopN() {
        ActivityList list = new ActivityList();
        // Deadlines in the future (adjust if needed)
        list.addActivity(new Task("Sooner", LocalDate.now().plusDays(1),
                LocalTime.parse("08:00"), 1));
        list.addActivity(new Task("Later", LocalDate.now().plusDays(5),
                LocalTime.parse("09:00"), 2));
        TestUi ui = new TestUi();
        new CheckCurrentCommand("checkcurrent 1").execute(list, ui, nb());
        assertTrue(ui.messages.stream().anyMatch(s -> s.contains("Top 1 closest tasks")));
    }
}
