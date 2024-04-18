package application;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class EarlyLectures_ForkJoin extends RecursiveTask<Boolean> {
    private static final int THRESHOLD = 5;
    private ArrayList<Class> classes;

    public EarlyLectures_ForkJoin(ArrayList<Class> classes) {
        this.classes = classes;
    }

    @Override
    protected Boolean compute() {
        if (classes.size() <= THRESHOLD) {
            return shiftClassesToEarlyMorning(classes);
        } else {
            Map<String, ArrayList<Class>> classesByDay = new HashMap<>();
            for (Class cls : classes) {
                String day = cls.getDay();
                classesByDay.computeIfAbsent(day, k -> new ArrayList<>()).add(cls);
            }

            ArrayList<EarlyLectures_ForkJoin> subtasks = new ArrayList<>();
            for (ArrayList<Class> classesForDay : classesByDay.values()) {
                subtasks.add(new EarlyLectures_ForkJoin(classesForDay));
            }

            invokeAll(subtasks);

            for (EarlyLectures_ForkJoin subtask : subtasks) {
                if (!subtask.join()) {
                    return false; 
                }
            }
            return true; 
        }
    }

    private boolean shiftClassesToEarlyMorning(ArrayList<Class> classesForDay) {
        if (isTimeSlotAvailable(classesForDay)) {
            LocalTime shiftTime = LocalTime.parse("09:00");
            for (Class cls : classesForDay) {
                cls.setStart(shiftTime.toString());
                cls.setEnd(shiftTime.plusHours(1).toString());
                shiftTime = shiftTime.plusHours(1);
            }
            return true;
        } else {
            System.out.println("Early morning timeslot for day " + classesForDay.get(0).getDay() + " is occupied. Cannot shift classes.");
            return false;
        }
    }

    private boolean isTimeSlotAvailable(ArrayList<Class> classes) {
        for (Class cls : classes) {
            if (cls.getStart().compareTo("09:00") >= 0 && cls.getEnd().compareTo("13:00") <= 0) {
                return false;
            }
        }
        return true;
    }

}
