package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.parser.Parser;
import astra.testutil.TestUi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GpaCommandsTest {

    @TempDir Path temp;

    private Notebook nb() {
        return new Notebook(temp.resolve("data.txt").toString());
    }

    @Test
    public void addListComputeDelete_flow_success() throws Exception {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        Notebook nb = nb();

        Parser.parse("add gpa cs2040c a+ 4mc").execute(list, ui, nb);
        Parser.parse("add gpa geq1000 s 4").execute(list, ui, nb);
        Parser.parse("add gpa ma2001 b 4").execute(list, ui, nb);

        // list
        ui.messages.clear();
        Parser.parse("list gpa").execute(list, ui, nb);
        assertFalse(ui.messages.isEmpty());
        String listing = ui.messages.get(ui.messages.size() - 1);
        assertTrue(listing.contains("1. CS2040C | A+ | 4 MC"));
        assertTrue(listing.contains("2. GEQ1000 | S | 4 MC"));
        assertTrue(listing.contains("3. MA2001 | B | 4 MC"));

        // compute GPA; S entry excluded; (5.0*4 + 3.5*4) / 8 = 4.25
        ui.messages.clear();
        Parser.parse("gpa").execute(list, ui, nb);
        assertFalse(ui.messages.isEmpty());
        assertEquals("Current GPA: 4.25", ui.messages.get(ui.messages.size() - 1));

        // delete S/U entry (index 2)
        ui.messages.clear();
        Parser.parse("delete gpa 2").execute(list, ui, nb);
        assertFalse(ui.messages.isEmpty());
        assertTrue(ui.messages.get(0).contains("Deleted GPA entry"));

        // compute again unchanged
        ui.messages.clear();
        Parser.parse("gpa").execute(list, ui, nb);
        assertEquals("Current GPA: 4.25", ui.messages.get(ui.messages.size() - 1));
    }

    @Test
    public void addGpa_invalidGrade_showsError() throws Exception {
        ActivityList list = new ActivityList();
        TestUi ui = new TestUi();
        Notebook nb = nb();

        Parser.parse("add gpa cs1231x hh 4").execute(list, ui, nb);
        assertTrue(ui.errors.stream().anyMatch(s -> s.toLowerCase().contains("invalid grade")));
    }
}

