package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.exception.FileSystemException;
import astra.exception.GpaInputException;
import astra.gpa.GpaEntry;
import astra.ui.Ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AddGpaCommand implements Command {
    private static final Set<String> VALID_GRADES = new HashSet<>(Arrays.asList(
            "A+","A","A-","B+","B","B-","C+","C","D+","D","F","S","U"
    ));

    private final String input;

    public AddGpaCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] parts = input.trim().split("\\s+");
            if (parts.length < 5) { // expect: add gpa SUBJECT GRADE MC
                ui.showError("Usage: add gpa <SUBJECT> <GRADE> <MC> (e.g., add gpa CS2040C A+ 4mc)");
                return false;
            }
            if (!parts[0].equalsIgnoreCase("add") || !parts[1].equalsIgnoreCase("gpa")) {
                ui.showError("Usage: add gpa <SUBJECT> <GRADE> <MC>");
                return false;
            }
            String subject = parts[2].toUpperCase(Locale.ROOT);
            String grade = parts[3].toUpperCase(Locale.ROOT);
            if (!VALID_GRADES.contains(grade)) {
                ui.showError("Invalid grade. Allowed: A+, A, A-, B+, B, B-, C+, C, D+, D, F, S, U");
                return false;
            }
            String mcToken = parts[4];
            String digits = mcToken.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) {
                ui.showError("Invalid MC. Provide a number like '4' or '4mc'.");
                return false;
            }
            int mc = Integer.parseInt(digits);
            if (mc < 0) {
                ui.showError("MC must be non-negative.");
                return false;
            }
            GpaEntry entry;
            try {
                entry = new GpaEntry(subject, grade, mc);
            } catch (GpaInputException e) {
                ui.showError(e.getMessage());
                return false;
            }
            notebook.getGpaList().add(entry);
            notebook.saveGpa();
            ui.showMessage("Added GPA entry: " + entry.toString());
        } catch (FileSystemException e) {
            ui.showError(e.getMessage());
        } catch (Exception e) {
            ui.showError("Failed to add GPA entry.");
        }
        return false;
    }
}
