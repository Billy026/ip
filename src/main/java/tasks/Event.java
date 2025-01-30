package tasks;

/**
 * Tasks with two datess.
 * 
 * @param start date event will start.
 * @param end date event will end.
 */
public class Event extends Task {
    private String start;
    private String end;

    /**
     * Constructor for newly added Events.
     * 
     * @param taskName name of task.
     * @param start date event will start.
     * @param end date event will end.
     */
    public Event(String taskName, String start, String end) {
        super(taskName, "E");
        this.start = start;
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
        super(taskName, "E", isCompleted);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return super.toString() + " | from: " + this.start + " | to: " + this.end;
    }
}