package tasks;

/**
 * Tasks with only one date.
 * 
 * @param time date to finish by.
 */
public class Deadline extends Task {
    private String time;

    /**
     * Constructor for newly added Deadlines.
     * 
     * @param taskName name of task.
     * @param time date to finish by.
     */
    public Deadline(String taskName, String time) {
        super(taskName, "D");
        this.time = time;
    }

    /**
     * Constructor for Deadlines loaded from save file.
     * 
     * @param taskName name of task.
     * @param time date to finish by.
     * @param isCompleted completion status of task.
     */
    public Deadline(String taskName, String time, boolean isCompleted) {
        super(taskName, "D", isCompleted);
        this.time = time;
    }

    @Override
    public String toString() {
        return super.toString() + " | by: " + this.time;
    }
}