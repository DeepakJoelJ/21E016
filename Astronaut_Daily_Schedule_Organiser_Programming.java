import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Task class
class Task {
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private String priority;
    private boolean isCompleted;

    public Task(String description, LocalTime startTime, LocalTime endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %s [%s]%s",
                startTime, endTime, description, priority, isCompleted ? " (Completed)" : "");
    }
}

// TaskFactory class
class TaskFactory {
    public static Task createTask(String description, String startTime, String endTime, String priority) {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        return new Task(description, start, end, priority);
    }
}

// Observer interface
interface Observer {
    void update(String message);
}

// ConsoleObserver class
class ConsoleObserver implements Observer {
    @Override
    public void update(String message) {
        System.out.println("Notification: " + message);
    }
}

// Singleton ScheduleManager class
class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;
    private List<Observer> observers;

    private ScheduleManager() {
        tasks = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void addTask(Task task) {
        if (isConflict(task)) {
            notifyObservers("Error: Task conflicts with existing task.");
            return;
        }
        tasks.add(task);
        notifyObservers("Task added successfully. No conflicts.");
    }

    public void removeTask(String description) {
        Optional<Task> taskToRemove = tasks.stream()
                .filter(task -> task.getDescription().equals(description))
                .findFirst();
        if (taskToRemove.isPresent()) {
            tasks.remove(taskToRemove.get());
            notifyObservers("Task removed successfully.");
        } else {
            notifyObservers("Error: Task not found.");
        }
    }

    public void editTask(String description, String newDescription, String newStartTime, String newEndTime, String newPriority) {
        Optional<Task> taskToEdit = tasks.stream()
                .filter(task -> task.getDescription().equals(description))
                .findFirst();
        if (taskToEdit.isPresent()) {
            Task task = taskToEdit.get();
            task.setDescription(newDescription);
            task.setStartTime(LocalTime.parse(newStartTime));
            task.setEndTime(LocalTime.parse(newEndTime));
            task.setPriority(newPriority);
            notifyObservers("Task edited successfully.");
        } else {
            notifyObservers("Error: Task not found.");
        }
    }

    public void markTaskAsCompleted(String description) {
        Optional<Task> taskToMark = tasks.stream()
                .filter(task -> task.getDescription().equals(description))
                .findFirst();
        if (taskToMark.isPresent()) {
            Task task = taskToMark.get();
            task.markAsCompleted();
            notifyObservers("Task marked as completed.");
        } else {
            notifyObservers("Error: Task not found.");
        }
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
            return;
        }
        Collections.sort(tasks, Comparator.comparing(Task::getStartTime));
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void viewTasksByPriority(String priority) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority().equalsIgnoreCase(priority)) {
                filteredTasks.add(task);
            }
        }
        if (filteredTasks.isEmpty()) {
            System.out.println("No tasks with the priority " + priority + " scheduled for the day.");
            return;
        }
        Collections.sort(filteredTasks, Comparator.comparing(Task::getStartTime));
        for (Task task : filteredTasks) {
            System.out.println(task);
        }
    }

    private boolean isConflict(Task newTask) {
        for (Task task : tasks) {
            if (task.getEndTime().isAfter(newTask.getStartTime()) && task.getStartTime().isBefore(newTask.getEndTime())) {
                return true;
            }
        }
        return false;
    }
}

// Main class
public class AstronautScheduleOrganizer {
    public static void main(String[] args) {
        ScheduleManager scheduleManager = ScheduleManager.getInstance();
        ConsoleObserver observer = new ConsoleObserver();
        scheduleManager.addObserver(observer);

        try {
           A Task task1 = TaskFactory.createTask("Morning Exercise", "07:00", "08:00", "High");
            scheduleManager.addTask(task1);

            Task task2 = TaskFactory.createTask("Team Meeting", "09:00", "10:00", "Medium");
            scheduleManager.addTask(task2);

            Task task3 = TaskFactory.createTask("Training Session", "09:30", "10:30", "High");
            scheduleManager.addTask(task3); // This should cause a conflict

            scheduleManager.viewTasks();

            scheduleManager.removeTask("Morning Exercise");
            scheduleManager.viewTasks();

            Task task4 = TaskFactory.createTask("Lunch Break", "12:00", "13:00", "Low");
            scheduleManager.addTask(task4);

            scheduleManager.viewTasks();
        } catch (DateTimeParseException e) {
            observer.update("Error: Invalid time format.");
        }
    }
}
