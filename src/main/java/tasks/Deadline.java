package tasks;

import exceptions.InvalidTaskOperationException;

/**
 * Tasks with only one date/time.
 * 
 * @param time date/time to finish by.
 */
public class Deadline extends Task {
    private String time;

    /**
     * Constructor for newly added Deadlines.
     * 
     * @param taskName name of task.
     * @param time date/time to finish by.
     * @throws InvalidTaskOperationException Invalid date/time given.
     */
    public Deadline(String taskName, String time) throws InvalidTaskOperationException {
        super(taskName, "D");

        // Check correct formatting of constructor command
        if (time.equals("")) {
            throw new InvalidTaskOperationException(
                    "You did not provide a date or time.\n" +
                    "    Please format your input as: deadline <task name> /by <date/time>.");
        }

        this.time = time;
    }

    /**
     * Constructor for Deadlines loaded from save file.
     * 
     * @param taskName name of task.
     * @param time date/time to finish by.
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