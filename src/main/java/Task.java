/**
 * Tasks in a to-do list
 */
public class Task {
    // Name of task
    private String taskName;
    // Status of completion
    private boolean completed;

    /**
     * Primary constructor
     * 
     * @param taskName name of task
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.completed = false;
    }

    /**
     * Sets completed to true
     * 
     * @throws InvalidTaskOperationException
     * When task has already been completed
     */
    public void check() throws InvalidTaskOperationException {
        if (this.completed) {
            throw new InvalidTaskOperationException("Task has already been completed.");
        } else {
            this.completed = true;
        }
    }

    /**
     * Sets completed to false
     * 
     * @throws InvalidTaskOperationException
     * When task has not been completed
     */
    public void uncheck() throws InvalidTaskOperationException {
        if (!this.completed) {
            throw new InvalidTaskOperationException("Task has still not been completed.");
        } else {
            this.completed = false;
        }
    }

    /**
     * Displays task with the status of its completion
     * 
     * @return taskName with completed status
     */
    public String listTask() {
        return "[" + ((completed) ? "X" : " ") + "] " + this.taskName;
    }
}