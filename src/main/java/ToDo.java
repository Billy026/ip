/**
 * Tasks without date/time
 */
public class ToDo extends Task {
    /**
     * Primary constructor
     * 
     * @param taskName name of task
     */
    public ToDo(String taskName) {
        super(taskName);
    }

    /**
     * Displays task with the status of its completion
     * 
     * @return taskName with completed status
     */
    @Override
    public String listTask() {
        return "[T]" + super.listTask();
    }
}