package astra.activity;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class TutorialTest {

    @Test
    public void fields_toString_writeToFile_ok() {
        Tutorial tut = new Tutorial("CS2113", "COM1", DayOfWeek.FRIDAY,
                LocalTime.parse("14:00"), LocalTime.parse("15:00"));
        assertEquals(DayOfWeek.FRIDAY, tut.getDay());
        assertEquals("COM1", tut.getVenue());
        assertTrue(tut.toString().contains("1400H"));
        assertTrue(tut.toString().contains("1500H"));
        assertTrue(tut.writeToFile().startsWith("Tutorial, "));
    }
}