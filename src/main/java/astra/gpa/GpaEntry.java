package astra.gpa;

import java.util.Locale;
import astra.exception.GpaInputException;

public class GpaEntry {
    private final String subject; // uppercase single token
    private final String grade;   // uppercase e.g., A+, B, S, U
    private final int mc;         // module credits (course units)

    public GpaEntry(String subject, String grade, int mc) {
        if (subject == null || subject.isBlank() || subject.contains(" ")) {
            throw new GpaInputException("Subject must be a single non-empty token.");
        }
        String g = grade == null ? "" : grade.trim().toUpperCase(Locale.ROOT);
        if (g.isEmpty()) {
            throw new GpaInputException("Grade must not be empty.");
        }
        if (mc < 0) {
            throw new GpaInputException("MC must be non-negative.");
        }
        this.subject = subject.trim().toUpperCase(Locale.ROOT);
        this.grade = g;
        this.mc = mc;
    }

    public String getSubject() {
        return subject;
    }

    public String getGrade() {
        return grade;
    }

    public int getMc() {
        return mc;
    }

    public boolean isSu() {
        return grade.equals("S") || grade.equals("U");
    }

    public double gradePoints() {
        return switch (grade) {
            case "A+", "A" -> 5.0;
            case "A-" -> 4.5;
            case "B+" -> 4.0;
            case "B" -> 3.5;
            case "B-" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D+" -> 1.5;
            case "D" -> 1.0;
            case "F" -> 0.0;
            case "S", "U" -> Double.NaN; // indicates excluded
            default -> throw new GpaInputException("Invalid grade: " + grade);
        };
    }

    public String toPipe() {
        return String.join(" | ", "GPA", subject, grade, String.valueOf(mc));
    }

    public String toCsv() {
        return String.join(",", subject, grade, String.valueOf(mc));
    }

    @Override
    public String toString() {
        return subject + " | " + grade + " | " + mc + " MC";
    }
}
