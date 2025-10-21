package astra.command;

import astra.activity.ActivityList;
import astra.data.Notebook;
import astra.gpa.GpaEntry;
import astra.ui.Ui;
import astra.exception.GpaInputException;

public class DeleteGpaCommand implements Command {
    private final String input;

    public DeleteGpaCommand(String input) {
        this.input = input;
    }

    @Override
    public boolean execute(ActivityList activities, Ui ui, Notebook notebook) {
        try {
            String[] parts = input.trim().split("\\s+");
            if (parts.length < 3) {
                ui.showError("Usage: delete gpa <INDEX>");
                return false;
            }
            if (!parts[0].equalsIgnoreCase("delete") || !parts[1].equalsIgnoreCase("gpa")) {
                ui.showError("Usage: delete gpa <INDEX>");
                return false;
            }
            int idx = Integer.parseInt(parts[2]);
            GpaEntry removed = notebook.getGpaList().remove(idx);
            notebook.saveGpa();
            ui.showMessage("Deleted GPA entry: " + removed.toString());
        } catch (NumberFormatException e) {
            ui.showError("Index must be a positive integer.");
        } catch (GpaInputException e) {
            ui.showError(e.getMessage());
        } catch (Exception e) {
            ui.showError("Failed to delete GPA entry.");
        }
        return false;
    }
}
