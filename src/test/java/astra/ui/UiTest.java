package astra.ui;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class UiTest {

    @Test
    public void showHelp_printsContent() {
        Ui ui = new Ui();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream prev = System.out;
        System.setOut(new PrintStream(out));
        try {
            ui.showHelp();
        } finally {
            System.setOut(prev);
        }
        String s = out.toString();
        assertTrue(s.contains("ASTRA - Notebook Possibilities"));
        assertTrue(s.toLowerCase().contains("task"));
    }
}
