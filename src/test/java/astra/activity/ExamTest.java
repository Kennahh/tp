package astra.activity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ExamTest {

    @Test
    public void fields_toString_writeToFile_ok() {
        Exam ex = new Exam("Finals", "MPSH", LocalDate.parse("2025-11-25"),
                LocalTime.parse("09:00"), LocalTime.parse("11:00"));
        assertEquals(LocalDate.parse("2025-11-25"), ex.getDate());
        assertEquals("MPSH", ex.getVenue());
        assertTrue(ex.toString().contains("Date"));
        assertTrue(ex.writeToFile().startsWith("Exam, "));
    }
}