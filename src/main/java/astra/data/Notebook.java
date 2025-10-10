package astra.data;

import astra.activity.Activity;
import astra.activity.ActivityList;
import astra.activity.Exam;
import astra.activity.Lecture;
import astra.activity.Task;
import astra.activity.Tutorial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Notebook {
    private String filePath;

    public Notebook(String filePath) {
        this.filePath = filePath;
    }

    public void saveToFile(ActivityList activities) throws IOException{
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        FileWriter fw = new FileWriter(filePath);
        for (int i = 0; i < activities.getListSize(); i++) {
            Activity activity = activities.getAnActivity(i);
            fw.write(activity.writeToFile() + "\n");
        }
        fw.close();
    }

    public ActivityList loadFile() throws FileNotFoundException {
        ActivityList activities = new ActivityList();
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        Scanner fileReader = new Scanner(file);
        String line;
        while (fileReader.hasNext()) {
            line = fileReader.nextLine();
            addTaskFromFile(line, activities);
        }
        return activities;
    }

    // type, description,...
    private void addTaskFromFile(String line, ActivityList activities) throws FileNotFoundException {
        String[] splitLine = line.split(",", 2);
        String type = splitLine[0].trim().toLowerCase();
        String[] details = splitLine[1].split(",");
        switch (type) {
        case "lecture":
            // type, description, venue, day, start time, end time
            Lecture lecture = new Lecture(details[0].trim(), details[1].trim(), details[2].trim(),
                    LocalTime.parse(details[3].trim()), LocalTime.parse(details[4].trim()));
            activities.addActivity(lecture);
        case "exam":
            // type, description, venue, day, start time, end time
            Exam exam = new Exam(details[0].trim(), details[1].trim(), LocalDate.parse(details[2].trim()),
                    LocalTime.parse(details[3].trim()), LocalTime.parse(details[4].trim()));
            activities.addActivity(exam);
        case "task":
            // type, description, deadline date, deadline time, isComplete
            Task task = new Task(details[0].trim(), LocalDate.parse(details[1].trim()), LocalTime.parse(details[2].trim()));
            if (details[3].trim().equals("1")) {
                task.setComplete(true);
            }
            activities.addActivity(task);
        case "tutorial":
            // type, description, venue, day, start time, end time
            Tutorial tutorial = new Tutorial(details[0].trim(), details[1].trim(), details[2].trim(),
                    LocalTime.parse(details[3].trim()), LocalTime.parse(details[4].trim()));
            activities.addActivity(tutorial);
        default:
            throw new FileNotFoundException("Invalid activity type in text file.");
        }
    }
}
