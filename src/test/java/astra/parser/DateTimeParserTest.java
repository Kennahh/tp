package astra.parser;

import astra.exception.InputException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DateTimeParserTest {
    @Test
    public void parseDate_formats_successAndDefault() throws Exception{
        LocalDate dt = DateTimeParser.parseDate("2025/12/10");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("2025-12-10");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("2025 11 25");
        parseDateTestHelper(dt, 2025, 11, 25);
        dt = DateTimeParser.parseDate("2025/dec/10");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("2025 December 10");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("December-10-2025");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("Dec 10 2025");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("10 December 2025");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("10 Dec 2025");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("December 10");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("Dec 10");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("10 Dec");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("10-dec");
        parseDateTestHelper(dt, 2025, 12, 10);
        dt = DateTimeParser.parseDate("1-jan");
        parseDateTestHelper(dt, 2025, 1, 1);
        dt = DateTimeParser.parseDate("2-jan");
        parseDateTestHelper(dt, 2025, 1, 2);
    }

    @Test
    public void parseDate_formats_failure() {
        try {
            LocalDate dt = DateTimeParser.parseDate(" ");
            parseDateTestHelper(dt, 1, 1, 1);
            fail("Expected InputException to be thrown");
        } catch (InputException e) {
            // test pass
        }
    }

    private void parseDateTestHelper(LocalDate dt, int year, int month, int day) {
        assertEquals(year, dt.getYear());
        assertEquals(month, dt.getMonthValue());
        assertEquals(day, dt.getDayOfMonth());
    }
}
