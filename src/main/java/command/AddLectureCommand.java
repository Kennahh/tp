package command;

import java.time.LocalTime;

public class AddLectureCommand extends AddCommand {
    private String day;
    private String venue;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public void execute() {

    }
}
