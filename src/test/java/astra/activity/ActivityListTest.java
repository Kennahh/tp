package astra.activity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ActivityListTest {

    @Test
    public void addGetDelete_basicFlow_success() {
        ActivityList list = new ActivityList();
        assertEquals(0, list.getListSize());

        Task t = new Task("Do CS", LocalDate.parse("2025-10-10"), LocalTime.parse("23:59"), 1);
        list.addActivity(t);
        assertEquals(1, list.getListSize());
        assertSame(t, list.getActivity(0));
        assertSame(t, list.getAnActivity(0));

        list.deleteActivity(0);
        assertEquals(0, list.getListSize());
    }
}
