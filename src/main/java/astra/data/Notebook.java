package astra.data;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Exam;
import astra.activity.Lecture;
import astra.activity.Task;
import astra.activity.Tutorial;
import astra.exception.FileSystemException;
import astra.gpa.GpaEntry;
import astra.gpa.GpaList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Notebook {
    // Delimiters
    private static final String SEP = " | ";
    private static final String SPLIT_REGEX = "\\s*\\|\\s*";

    private String filePath;
    private String erroredTaskLines = ""; // stores errored lines when reading
    private String erroredGpaLines = ""; // stores errored GPA

    // GPA related
    private final String gpaTxtPath;
    private final String gpaCsvPath;
    private final GpaList gpaList = new GpaList();

    public Notebook(String filePath) {

        this.filePath = filePath;

        // derive GPA file paths in same directory
        File f = new File(filePath);
        File dir = f.getParentFile();
        String baseDir = dir == null ? "." : dir.getPath();
        this.gpaTxtPath = baseDir + File.separator + "gpa.txt";
        this.gpaCsvPath = baseDir + File.separator + "gpa.csv";

    }

    public void loadGpa() {
        // attempt to load existing GPA entries
        try {
            List<GpaEntry> loadedGpa = loadGpaFromFile();
            for (GpaEntry e : loadedGpa) {
                gpaList.add(e);
            }
        } catch (FileSystemException e) {
            // ignore load errors but keep list empty; caller side handles messages if needed
        }
    }

    /**
     * Rewrite the data file according to the activities list in csv format.
     *
     * @param activities list of activities to be written to the text file
     * @throws IOException if the file is not found
     */
    public void saveToFile(ActivityList activities) throws IOException{
        String csvFilePath = filePath.substring(0, filePath.length() - 3) + "csv";
        File file = new File(csvFilePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        try (FileWriter fw = new FileWriter(csvFilePath)) {
            fw.write("Task, Description, Deadline Date, Deadline Time, Priority, Status" + "\n");
            fw.write("Other Types, Description, Venue, Date or Day, Start Time, End Time" + "\n\n");
            for (int i = 0; i < activities.getListSize(); i++) {
                Activity activity = activities.getAnActivity(i);
                fw.write(activity.writeToFile() + "\n");
            }
        }
    }

    /**
     * Load the text file (csv) containing activities
     *
     * @return an ActivityList containing all the activities in the text file
     * @throws FileNotFoundException if the text file is not found
     */
    public ActivityList loadFile() throws FileSystemException, FileNotFoundException {
        ActivityList activities = new ActivityList();
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        if (!file.exists()) {
            return activities;
        }
        try (Scanner fileReader = new Scanner(file)) {
            while (fileReader.hasNext()) {
                String line = fileReader.nextLine();
                addTaskFromFile(line, activities);
            }
        }
        return activities;
    }

    /**
     * Add one activity to the activities list
     *
     * @param line one line in the text file
     * @param activities the target activities list to add an activity
     * @throws FileSystemException if the type of the activity is not among Lecture, Exam, Task and Tutorial
     */
    // type, description,...
    private void addTaskFromFile(String line, ActivityList activities) throws FileSystemException {
        String[] splitLine = line.split(",", 2);
        String type = splitLine[0].trim().toLowerCase();
        String[] details = splitLine[1].split(",");

        DayOfWeek day;
        switch (type) {
        case "lecture":
            // type, description, venue, day, start time, end time
            assert details.length == 5: "Number of details for a lecture should be 5.";
            day = DayOfWeek.of(Integer.parseInt(details[2].trim()));
            Lecture lecture = new Lecture(details[0].trim(), details[1].trim(), day,
                    LocalTime.parse(details[3].trim()), LocalTime.parse(details[4].trim()));
            activities.addActivity(lecture);
            break;
        case "exam":
            // type, description, venue, day, start time, end time
            assert details.length == 5: "Number of details for an exam should be 5.";
            Exam exam = new Exam(details[0].trim(), details[1].trim(), LocalDate.parse(details[2].trim()),
                    LocalTime.parse(details[3].trim()), LocalTime.parse(details[4].trim()));
            activities.addActivity(exam);
            break;
        case "task":
            // type, description, deadline date, deadline time, isComplete, priority
            String desc = details[0].trim();
            LocalDate date = LocalDate.parse(details[1].trim());
            LocalTime time = LocalTime.parse(details[2].trim());

            boolean isComplete = false;
            int priority = 3; // default (normal priority)

            if (details.length >= 4) {
                try {
                    // If the 4th element is boolean-ish (0/1), treat it as complete flag
                    if (details[3].trim().equals("1") || details[3].trim().equalsIgnoreCase("true")) {
                        isComplete = true;
                    }
                    // If there are 5 or more elements, treat the 5th as priority
                    if (details.length >= 5) {
                        priority = Integer.parseInt(details[4].trim());
                    }
                } catch (NumberFormatException e) {
                    // If malformed priority, keep default 3
                    priority = 3;
                }
            }
            Task task = new Task(desc, date, time, priority);
            task.setComplete(isComplete);
            activities.addActivity(task);
            break;
        case "tutorial":
            // type, description, venue, day, start time, end time
            assert details.length == 5: "Number of details for a tutorial should be 5.";
            day = DayOfWeek.of(Integer.parseInt(details[2].trim()));
            Tutorial tutorial = new Tutorial(details[0].trim(), details[1].trim(), day,
                    LocalTime.parse(details[3].trim()), LocalTime.parse(details[4].trim()));
            activities.addActivity(tutorial);
            break;
        default:
            throw new FileSystemException("Invalid activity type in text file.");
        }
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

            Scanner s = new Scanner(f);
            String line = "";
            int lineNumber = 0;
            while (s.hasNextLine()) {
                try {
                    line = s.nextLine();
                    lineNumber += 1;
                    activities.add(parseLine(line));
                } catch (FileSystemException e) {
                    erroredTaskLines += lineNumber + ": " + line + '\n';
                }
            }
        } catch (IOException e) {
            throw new FileSystemException("[ERROR] Failed to read file: " + e.getMessage());
        }
        return activities;
    }

    /**
     * Save all activities to file (overwrite)
     *
     * @param activities List of activities to save.
     * @throws FileSystemException If writing fails.
     */
    public void writeToFile(List<Activity> activities) throws FileSystemException {
        try {
            // ensure directory exists
            File f = new File(filePath);
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter writer = new FileWriter(filePath)) { // overwrite
                for (Activity a : activities) {
                    writer.write(serializeActivity(a) + System.lineSeparator());
                }
            }
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

        try {
            switch (type) {
            case "TASK":
            case "T":
                if (parts.length < 5) {
                    throw new FileSystemException("[ERROR] Corrupted. Invalid task: " + line);
                }
                LocalDate d = LocalDate.parse(parts[3]);
                LocalTime tm = LocalTime.parse(parts[4]);
                Integer priority = Integer.parseInt(parts[5]);
                Task t = new Task(description, d, tm, priority);
                if (done) {
                    t.setIsComplete();
                }
                return t;
            case "LECTURE":
            case "LEC": {
                // LECTURE | done | description | venue | day | start | end
                if (parts.length < 7) {
                    throw new FileSystemException("[ERROR] Corrupted. Invalid lecture: " + line);
                }
                String desc = parts[2];
                String venue = parts[3];
                DayOfWeek day = DayOfWeek.of(Integer.parseInt(parts[4]));
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
                String desc = parts[2];
                String venue = parts[3];
                DayOfWeek day = DayOfWeek.of(Integer.parseInt(parts[4]));
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
                String desc = parts[2];
                String venue = parts[3];
                LocalDate date = LocalDate.parse(parts[4]);
                LocalTime start = LocalTime.parse(parts[5]);
                LocalTime end = LocalTime.parse(parts[6]);
                Exam ex = new Exam(desc, venue, date, start, end);
                return ex;
            }

            default:
                throw new FileSystemException("[ERROR] Corrupted. Unknown task type: " + type);
            }
        } catch (Exception e) {
            throw new FileSystemException("[ERROR] Corrupted. Invalid lecture: " + line);
        }
    }

    /** 
     * Convert Task object into a file line
     *
     * @param a Task object to serialize.
     * @return String representation for file storage.
     */
    private String serializeActivity(Activity a) {
        String done = (a instanceof Task && ((Task) a).getIsComplete()) ? "1" : "0";
        if (a instanceof Lecture) {
            Lecture l = (Lecture) a;
            return String.join(SEP, "LECTURE", done,
                safe(l.getDescription()), safe(l.getVenue()), safe(String.valueOf(l.getDay().getValue())),
                safe(l.getStartTime().toString()), safe(l.getEndTime().toString()));
        } else if (a instanceof Tutorial) {
            Tutorial t = (Tutorial) a;
            return String.join(SEP, "TUTORIAL", done,
                safe(t.getDescription()), safe(t.getVenue()), safe(String.valueOf(t.getDay().getValue())),
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
                safe(t.getDeadlineTime().toString()),
                safe(String.valueOf(t.getPriority())));
        } else {
            // Generic fallback
            return String.join(SEP, "TASK", done, safe(a.toString()));
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    // GPA persistence and accessors
    public GpaList getGpaList() {
        return gpaList;
    }

    public List<GpaEntry> loadGpaFromFile() throws FileSystemException {
        List<GpaEntry> list = new ArrayList<>();
        File f = new File(gpaTxtPath);
        try {
            if (!f.exists()) {
                File parent = f.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                f.createNewFile();
                return list;
            }
            Scanner s = new Scanner(f);
            String line = "";
            int lineNumber = 0;
            while (s.hasNextLine()) {
                try {
                    line = s.nextLine().trim();
                    lineNumber += 1;
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split(SPLIT_REGEX);
                    if (parts.length != 4 || !parts[0].equalsIgnoreCase("GPA")) {
                        throw new FileSystemException("[ERROR] Corrupted. Invalid GPA line: " + line);
                    }
                    String subject = parts[1].trim();
                    String grade = parts[2].trim();
                    int mc = Integer.parseInt(parts[3].trim());
                    list.add(new GpaEntry(subject, grade, mc));
                } catch (FileSystemException e) {
                    erroredGpaLines += lineNumber + ": " + line + '\n';
                }
            }
        } catch (IOException | RuntimeException e) {
            if (e instanceof FileSystemException) {
                throw (FileSystemException) e;
            }
            throw new FileSystemException("[ERROR] Failed to read GPA: " + e.getMessage());
        }
        return list;
    }

    public void writeGpaToFile(List<GpaEntry> entries) throws FileSystemException {
        try {
            File f = new File(gpaTxtPath);
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter w = new FileWriter(gpaTxtPath)) {
                for (GpaEntry e : entries) {
                    w.write(e.toPipe());
                    w.write(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new FileSystemException("[ERROR] Failed to save GPA: " + e.getMessage());
        }
    }

    public void writeGpaCsv(List<GpaEntry> entries) throws FileSystemException {
        try {
            File f = new File(gpaCsvPath);
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter w = new FileWriter(gpaCsvPath)) {
                w.write("Subject,Grade,MC\n");
                for (GpaEntry e : entries) {
                    w.write(e.toCsv());
                    w.write(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new FileSystemException("[ERROR] Failed to save GPA CSV: " + e.getMessage());
        }
    }

    public void saveGpa() throws FileSystemException {
        writeGpaToFile(gpaList.toList());
        writeGpaCsv(gpaList.toList());
    }

    public String getErrors() {
        String errors = "";
        if (!erroredTaskLines.isEmpty()) {
            errors += "[WARNING!] Detected errors in saved activities! See lines:\n"
                    + erroredTaskLines
                    + "These line(s) will be deleted if any activity is added, deleted or modified!\n\n";
        }
        if (!erroredGpaLines.isEmpty()) {
            errors += "[WARNING!] Detected errors in saved GPA! See lines:\n"
                    + erroredGpaLines
                    + "These line(s) will be deleted if any gpa is added, deleted or modified!";
        }
        return errors;
    }
}
