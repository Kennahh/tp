package astra.data;

import astra.activity.*;
import astra.exception.FileSystemException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotebookTest {

    @TempDir Path temp;

    @Test
    public void writeThenLoad_pipeFormat_roundTrip_ok() throws Exception {
        String path = temp.resolve("pipe.txt").toString();
        Notebook nb = new Notebook(path);

        ActivityList list = new ActivityList();
        Task t = new Task("Do HW", LocalDate.parse("2025-10-10"), LocalTime.parse("23:59"));
        t.setIsComplete();
        Lecture l = new Lecture("Lecture", "LT19", DayOfWeek.MONDAY, LocalTime.parse("10:00"), LocalTime.parse("12:00"));
        Tutorial tut = new Tutorial("Tutorial", "COM1", DayOfWeek.FRIDAY, LocalTime.parse("14:00"), LocalTime.parse("15:00"));
        Exam ex = new Exam("Finals", "MPSH", LocalDate.parse("2025-11-25"), LocalTime.parse("09:00"), LocalTime.parse("11:00"));
        list.addActivity(t); list.addActivity(l); list.addActivity(tut); list.addActivity(ex);

        nb.writeToFile(list.toList());
        List<Activity> loaded = new Notebook(path).loadFromFile();
        assertEquals(4, loaded.size());

        Task t2 = (Task) loaded.get(0);
        assertEquals("Do HW", t2.getDescription());
        assertTrue(t2.getIsComplete());

        Lecture l2 = (Lecture) loaded.get(1);
        assertEquals("LT19", l2.getVenue());

        Tutorial tut2 = (Tutorial) loaded.get(2);
        assertEquals(DayOfWeek.FRIDAY, tut2.getDay());

        Exam ex2 = (Exam) loaded.get(3);
        assertEquals(LocalDate.parse("2025-11-25"), ex2.getDate());
    }

    @Test
    public void load_csvFormat_manualContent_ok() throws Exception {
        Path p = temp.resolve("csv.txt");
        // Matches loadFile() expectations (note: DayOfWeek as number; date/time ISO)
        String content = String.join(System.lineSeparator(),
                "Task, Read, 2025-10-10, 23:59, 1",
                "Lecture, CS2113, LT19, 1, 10:00, 12:00",
                "Tutorial, T1, COM1-0210, 5, 14:00, 15:00",
                "Exam, Finals, MPSH, 2025-11-25, 09:00, 11:00"
        );
        Files.writeString(p, content);

        Notebook nb = new Notebook(p.toString());
        ActivityList list = nb.loadFile();
        assertEquals(4, list.getListSize());
        assertTrue(list.getActivity(0) instanceof Task);
        assertTrue(list.getActivity(1) instanceof Lecture);
        assertTrue(list.getActivity(2) instanceof Tutorial);
        assertTrue(list.getActivity(3) instanceof Exam);
    }

    @Test
    public void loadFromFile_corruptedLine_throws() throws Exception {
        Path p = temp.resolve("bad.txt");
        Files.writeString(p, "BAD | 0 | x");
        Notebook nb = new Notebook(p.toString());
        assertThrows(FileSystemException.class, nb::loadFromFile);
    }
}