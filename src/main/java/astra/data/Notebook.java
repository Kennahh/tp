package astra.data;

import astra.activity.*;
import astra.exception.FileSystemException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.time.LocalDate;
import java.time.LocalTime;

/*
 * Storage class saves the tasks in the hard disk automatically whenever the task list changes
 * and load the tasks from the hard disk when the program starts
 */
public class Notebook {

    private final String filePath;
    // Delimiters
    private static final String SEP = " | ";
    private static final String SPLIT_REGEX = "\\s*\\|\\s*";

    /**
     * Creates a Notebook bound to a file path.
     *
     * @param filePath Path of the data file.
     */
    public Notebook(String filePath) {
        this.filePath = filePath;
    }   

    /**
     * Loads tasks from the file system.
     *
     * @return List of tasks loaded.
     * @throws FileSystemException If reading or parsing fails.
     */
    public List<Activity> loadFromFile() throws FileSystemException {
        List<Activity> activities = new ArrayList<>();
        File f = new File(filePath); // create a File for the given file path

        try {
            if (!f.exists()) {
                // create parent directory if it doesn't exist
                File parent = f.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                f.createNewFile();
                return activities; // empty list
            }

            Scanner s = new Scanner(f); // create a Scanner using the File as the source
            while (s.hasNextLine()) {
                String line = s.nextLine();
                activities.add(parseLine(line));
            }
            s.close();
        } catch(IOException e) {
            throw new FileSystemException("[ERROR] Failed to read file: " + e.getMessage());
        }

        return activities;
    }

    /**
     * Save all tasks to file (overwrite)
     *
     * @param tasks List of tasks to save.
     * @throws FileSystemException If writing fails.
     */
    public void writeToFile(List<Activity> activities) throws FileSystemException {
        try {
            FileWriter writer = new FileWriter(filePath); // overwrite
            for (Activity a : activities) {
                writer.write(serializeActivity(a) + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new FileSystemException("[ERROR] Failed to save activities: " + e.getMessage());
        }
    }

    /**
     * Convert file line into corresponding Activity object and check if file is currupted
     * 
     * @param line Line from the data file.
     * @return Corresponding Activity object.
     */
    private Activity parseLine(String line) throws FileSystemException {
        if (line == null || line.trim().isEmpty()) {
            throw new FileSystemException("Corrupted. Empty activity line.");
        }
        String[] parts = line.split(SPLIT_REGEX, -1);
        if (parts.length < 3) {
            throw new FileSystemException("[ERROR] Corrupted. Invalid activity format: " + line);
        }

        String type = parts[0];
        boolean done = "1".equals(parts[1]);
        String description = parts[2];

        switch (type) {
        case "TASK":
        case "T": 
            if (parts.length < 5) {
                throw new FileSystemException("[ERROR] Corrupted. Invalid task: " + line);
            }
            LocalDate d = LocalDate.parse(parts[3]);
            LocalTime tm = LocalTime.parse(parts[4]);
            Task t = new Task(description, d, tm);
            if (done) t.setIsComplete();
            return t;
        case "LECTURE":
        case "LEC": {
            // LECTURE | done | description | venue | day | start | end
            if (parts.length < 7) {
                throw new FileSystemException("[ERROR] Corrupted. Invalid lecture: " + line);
            }
            String desc = parts[2], venue = parts[3], day = parts[4];
            LocalTime start = LocalTime.parse(parts[5]);
            LocalTime end = LocalTime.parse(parts[6]);
            Lecture lec = new Lecture(desc, venue, day, start, end);
            return lec;
        }
        case "TUTORIAL":
        case "TUT": {
            // TUTORIAL | done | description | venue | day | start | end
            if (parts.length < 7) {
                throw new FileSystemException("[ERROR] Corrupted. Invalid tutorial: " + line);
            }
            String desc = parts[2], venue = parts[3], day = parts[4];
            LocalTime start = LocalTime.parse(parts[5]);
            LocalTime end = LocalTime.parse(parts[6]);
            Tutorial tut = new Tutorial(desc, venue, day, start, end);
            return tut;
        }
        case "EXAM":
        case "EX": {
            // EXAM | done | description | venue | date | start | end
            if (parts.length < 7) {
                throw new FileSystemException("[ERROR] Corrupted. Invalid exam: " + line);
            }
            String desc = parts[2], venue = parts[3];
            LocalDate date = LocalDate.parse(parts[4]);
            LocalTime start = LocalTime.parse(parts[5]);
            LocalTime end = LocalTime.parse(parts[6]);
            Exam ex = new Exam(desc, venue, date, start, end);
            return ex;
        }

        default:
            throw new FileSystemException("[ERROR] Corrupted. Unknown task type: " + type);
        }
    }

    /** 
     * Convert Task object into a file line
     *
     * @param t Task object to serialize.
     * @return String representation for file storage.
     */
    private String serializeActivity(Activity a) {
        String done = (a instanceof Task && ((Task) a).getIsComplete()) ? "1" : "0";
        if (a instanceof Lecture) {
            Lecture l = (Lecture) a;
            return String.join(SEP, "LECTURE", done,
                safe(l.getDescription()), safe(l.getVenue()), safe(l.getDay()),
                safe(l.getStartTime().toString()), safe(l.getEndTime().toString()));
        } else if (a instanceof Tutorial) {
            Tutorial t = (Tutorial) a;
            return String.join(SEP, "TUTORIAL", done,
                safe(t.getDescription()), safe(t.getVenue()), safe(t.getDay()),
                safe(t.getStartTime().toString()), safe(t.getEndTime().toString()));
        } else if (a instanceof Exam) {
            Exam ex = (Exam) a;
            return String.join(SEP, "EXAM", done,
                safe(ex.getDescription()), safe(ex.getVenue()),
                safe(ex.getDate().toString()),
                safe(ex.getStartTime().toString()), safe(ex.getEndTime().toString()));
        } else if (a instanceof Task) {
            Task t = (Task) a;
            return String.join(SEP, "TASK", done,
                safe(t.getDescription()),
                safe(t.getDeadlineDate().toString()),
                safe(t.getDeadlineTime().toString()));
        } else {
            // Generic fallback
            return String.join(SEP, "TASK", done, safe(a.toString()));
        }
    }

    private static String safe(String s) { return s == null ? "" : s; }
}
