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
        list.addActivity(new Task("submit report", LocalDate.parse("2025-10-10"),
                LocalTime.parse("23:59"), 1));
        list.addActivity(new Lecture("CS2113", "LT9", DayOfWeek.FRIDAY, LocalTime.parse("16:00"),
                LocalTime.parse("18:00")));
        list.addActivity(new Tutorial("CS2113 T1", "COM1", DayOfWeek.WEDNESDAY,
                LocalTime.parse("12:00"), LocalTime.parse("13:00")));
        TestUi ui  = new TestUi();
        new CheckExamCommand().execute(list, ui, nb());
        assertTrue(ui.messages.stream().anyMatch(s -> s.contains("No exams")));
    }

    @Test
    public void checkExam_singleExam_success() {
        ActivityList list = new ActivityList();
        list.addActivity(new Exam("CS2107 midterm", "MPSH1", LocalDate.parse("2025-10-20"),
                LocalTime.parse("14:00"), LocalTime.parse("15:00")));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        String expectedOutput1 = "CS2107 midterm";
        String expectedOutput2 = " MPSH1";

        TestUi ui = new TestUi();
        new CheckExamCommand().execute(list, ui, nb());
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
    }

    @Test
    public void checkLectures_noLectureOnDay_noLectureMessage() {
        ActivityList list = new ActivityList();
        list.addActivity(new Task("submit report", LocalDate.parse("2025-10-10"), LocalTime.parse("23:59"), 1));
        list.addActivity(new Exam("CS2107 midterm", "MPSH1", LocalDate.parse("2025-10-20"),
                LocalTime.parse("14:00"), LocalTime.parse("15:00")));
        list.addActivity(new Tutorial("CS2113 T1", "COM1", DayOfWeek.WEDNESDAY, LocalTime.parse("12:00"),
                LocalTime.parse("13:00")));
        list.addActivity(new Lecture("CS2113", "LT9", DayOfWeek.FRIDAY, LocalTime.parse("16:00"),
                LocalTime.parse("18:00")));
        TestUi ui  = new TestUi();
        new CheckLecturesCommand("Mon").execute(list, ui, nb());
        assertTrue(ui.messages.stream().anyMatch(s -> s.contains("no lecture")));
    }

    @Test
    public void checkLectures_dayFilter_messageCount() {
        ActivityList list = new ActivityList();
        list.addActivity(new Lecture("L1", "LT", DayOfWeek.MONDAY, LocalTime.parse("10:00"),
                LocalTime.parse("12:00")));
        list.addActivity(new Lecture("L2", "LT", DayOfWeek.FRIDAY, LocalTime.parse("10:00"),
                LocalTime.parse("12:00")));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        String expectedOutput1 = "L1";
        String expectedOutput2 = "LT";

        TestUi ui = new TestUi();
        new CheckLecturesCommand("Mon").execute(list, ui, nb());
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
    }

    @Test
    public void checkTutorials_noTutorialOnDay_noTutorialMessage() {
        ActivityList list = new ActivityList();
        list.addActivity(new Task("submit report", LocalDate.parse("2025-10-10"), LocalTime.parse("23:59"), 1));
        list.addActivity(new Exam("CS2107 midterm", "MPSH1", LocalDate.parse("2025-10-20"),
                LocalTime.parse("14:00"), LocalTime.parse("15:00")));
        list.addActivity(new Tutorial("CS2113 T1", "COM1", DayOfWeek.WEDNESDAY, LocalTime.parse("12:00"),
                LocalTime.parse("13:00")));
        list.addActivity(new Lecture("CS2113", "LT9", DayOfWeek.FRIDAY, LocalTime.parse("16:00"),
                LocalTime.parse("18:00")));
        TestUi ui  = new TestUi();
        new CheckTutorialsCommand("Mon").execute(list, ui, nb());
        assertTrue(ui.messages.stream().anyMatch(s -> s.contains("no tutorial")));
    }

    @Test
    public void checkTutorials_dayFilter_messageCount() {
        ActivityList list = new ActivityList();
        list.addActivity(new Tutorial("T1", "COM1", DayOfWeek.FRIDAY, LocalTime.parse("14:00"),
                LocalTime.parse("15:00")));
        list.addActivity(new Tutorial("T2", "COM1", DayOfWeek.MONDAY, LocalTime.parse("14:00"),
                LocalTime.parse("15:00")));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        String expectedOutput1 = "T1";
        String expectedOutput2 = "COM1";

        TestUi ui = new TestUi();
        new CheckTutorialsCommand("Fri").execute(list, ui, nb());
        String output = outContent.toString();
        System.setOut(originalOut);
        assertTrue(output.contains(expectedOutput1));
        assertTrue(output.contains(expectedOutput2));
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
