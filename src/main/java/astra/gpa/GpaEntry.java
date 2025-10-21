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
        switch (grade) {
        case "A+":
        // fallthrough
        case "A":
            return 5.0;
        case "A-":
            return 4.5;
        case "B+":
            return 4.0;
        case "B":
            return 3.5;
        case "B-":
            return 3.0;
        case "C+":
            return 2.5;
        case "C":
            return 2.0;
        case "D+":
            return 1.5;
        case "D":
            return 1.0;
        case "F":
            return 0.0;
        case "S":
        // fallthrough
        case "U":
            return Double.NaN; // indicates excluded
        default:
            throw new GpaInputException("Invalid grade: " + grade);
        }
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
