package Bob.tasks;

/**
 * Tasks with only one date.
 */
public class Deadline extends TaskWithDeadline {
    /**
     * Constructor for newly added Deadlines.
     * 
     * @param taskName name of task.
     * @param by date to finish by.
     */
    public Deadline(String taskName, String by) {
        super(taskName, "D", by);
    }

    /**
     * Constructor for Deadlines loaded from save file.
     * 
     * @param taskName name of task.
     * @param by date to finish by.
     * @param isCompleted completion status of task.
     */
    public Deadline(String taskName, String by, boolean isCompleted) {
        super(taskName, "D", by, isCompleted);
    }

    @Override
    public String toString() {
        String[] parts = super.toString().split(",");
        return parts[0] + " | by: " + parts[1];
    }
}