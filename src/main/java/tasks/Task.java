package tasks;

import exceptions.InvalidTaskOperationException;

/**
 * Encapsulates a task with a name and completion status.
 * 
 * @param taskType type of task.
 * @param taskName name of task.
 * @param isCompleted completion status of task.
 */
public abstract class Task {
    private String taskName;
    private String taskType;
    private boolean isCompleted;

    /**
     * Constructor for newly added tasks.
     * 
     * @param taskName name of task.
     * @param taskType type of task.
     */
    public Task(String taskName, String taskType) {
        this.taskType = taskType;
        this.taskName = taskName;
        this.isCompleted = false;
    }

    /**
     * Constructor for tasks loaded from save file.
     * 
     * @param taskName
     * @param taskType
     * @param isCompleted
     */
    public Task(String taskName, String taskType, boolean isCompleted) {
        this.taskType = taskType;
        this.taskName = taskName;
        this.isCompleted = isCompleted;
    }

    /**
     * Sets completion status to true.
     * 
     * @throws InvalidTaskOperationException When task has already been completed.
     */
    public void check() throws InvalidTaskOperationException {
        if (this.isCompleted) {
            throw new InvalidTaskOperationException("Task has already been completed.");
        } else {
            this.isCompleted = true;
        }
    }

    /**
     * Sets completion status to false.
     * 
     * @throws InvalidTaskOperationException When task has not been completed.
     */
    public void uncheck() throws InvalidTaskOperationException {
        if (!this.isCompleted) {
            throw new InvalidTaskOperationException("Task has still not been completed.");
        } else {
            this.isCompleted = false;
        }
    }

    /**
     * Converts a line from save file to a Task object.
     * 
     * @param line line from save file.
     * @return saved Task object.
     * @throws IllegalArgumentException When save format is invalid.
     */
    public static Task fromSaveFormat(String line) throws IllegalArgumentException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("    Invalid save format: " + line);
        }

        boolean isCompleted = parts[0].trim().equals("[X]");
        String taskType = parts[1].trim();
        String taskName = parts[2].trim();

        switch (taskType) {
            case "T":
                return new ToDo(taskName, isCompleted);
            case "D":
                String time = parts[3].trim().replaceFirst("by: ", "");
                return new Deadline(taskName, time, isCompleted);
            case "E":
                String start = parts[3].trim().replaceFirst("from: ", "");
                String end = parts[4].trim().replaceFirst("to: ", "");
                return new Event(taskName, start, end, isCompleted);
            default:
                throw new IllegalArgumentException("    Invalid task type: " + taskType);
        }
    }

    /**
     * Returns whether task is the declared task type.
     * 
     * @param taskType declared task type.
     * @return If task is the declared task type.
     */
    public boolean isTaskType(String taskType) {
        return this.taskType.equals(taskType);
    }

    @Override
    public String toString() {
        return "[" + ((isCompleted) ? "X" : " ") + "] | " + this.taskType  + " | " + this.taskName;
    }
}