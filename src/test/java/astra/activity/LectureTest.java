package astra.activity;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LectureTest {

    @Test
    public void fields_toString_writeToFileOk() {
        Lecture lec = new Lecture("CS2113 Lec", "LT19", DayOfWeek.MONDAY,
                LocalTime.parse("10:00"), LocalTime.parse("12:00"));
        assertEquals(DayOfWeek.MONDAY, lec.getDay());
        assertEquals("LT19", lec.getVenue());
        assertTrue(lec.toString().contains("1000H"));
        assertTrue(lec.toString().contains("1200H"));
        assertTrue(lec.writeToFile().startsWith("Lecture, "));
    }
}
