package tasks;

import exceptions.InvalidTaskOperationException;

/**
 * Encapsulates a task with a name and completion status.
 * 
 * @param taskType type of task.
 * @param taskName name of task.
 * @param completed completion status of task.
 */
public abstract class Task {
    private String taskType;
    private String taskName;
    private boolean completed;

    /**
     * Primary constructor.
     * 
     * @param taskName name of task.
     * @param taskType type of task.
     */
    public Task(String taskName, String taskType) {
        this.taskType = taskType;
        this.taskName = taskName;
        this.completed = false;
    }

    /**
     * Sets completion status to true.
     * 
     * @throws InvalidTaskOperationException When task has already been completed.
     */
    public void check() throws InvalidTaskOperationException {
        if (this.completed) {
            throw new InvalidTaskOperationException("Task has already been completed.");
        } else {
            this.completed = true;
        }
    }

    /**
     * Sets completion status to false.
     * 
     * @throws InvalidTaskOperationException When task has not been completed.
     */
    public void uncheck() throws InvalidTaskOperationException {
        if (!this.completed) {
            throw new InvalidTaskOperationException("Task has still not been completed.");
        } else {
            this.completed = false;
        }
    }

    @Override
    public String toString() {
        return "[" + ((completed) ? "X" : " ") + "] | " + this.taskType  + " | " + this.taskName;
    }
}