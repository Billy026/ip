package tasks;

/**
 * Tasks without date/time
 * 
 * @param taskName name of task
 */
public class ToDo extends Task {
    /**
     * Primary constructor
     * 
     * @param taskName name of task
     */
    public ToDo(String taskName) {
        super(taskName, "T");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}