package bob.tasks;

/**
 * Task without a deadline
 */
public class ToDo extends Task {
    /**
     * Constructor for newly added ToDos.
     * 
     * @param taskName name of task
     */
    public ToDo(String taskName) {
        super(taskName, "T");
    }

    /**
     * Constructor for ToDos loaded from save file.
     * 
     * @param taskName name of task.
     * @param isCompleted completion status of task.
     */
    public ToDo(String taskName, boolean isCompleted) {
        super(taskName, "T", isCompleted);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}