package bob.tasks;

/**
 * Represents a task with a start and end date.
 * 
 * @param end date event will end.
 */
public class Event extends TaskWithDeadline {
    private String end;

    /**
     * Constructor for newly added Events.
     * 
     * @param taskName name of task.
     * @param start date event will start.
     * @param end date event will end.
     */
    public Event(String taskName, String start, String end) {
        super(taskName, "E", start);
        this.end = end;
    }

    /**
     * Constructor for Events loaded from save file.
     * 
     * @param taskName name of task.
     * @param start date event will start.
     * @param end date event will end.
     * @param isCompleted completion status of task.
     */
    public Event(String taskName, String start, String end, boolean isCompleted) {
        super(taskName, "E", start, isCompleted);
        this.end = end;
    }

    @Override
    public String toString() {
        String[] parts = super.toString().split(",");
        return parts[0] + " | from: " + parts[1] + " | to: " + this.end;
    }
}