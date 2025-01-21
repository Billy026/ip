/**
 * Tasks with only one date/time
 */
public class Deadline extends Task {
    private String time;

    /**
     * Primary constructor
     * 
     * @param taskName name of task
     * @param time date/time to finish by
     * 
     * @throws InvalidTaskOperationException
     * Invalid date/time given
     */
    public Deadline(String taskName, String time) throws InvalidTaskOperationException {
        super(taskName);
        if (time.equals("")) {
            throw new InvalidTaskOperationException(
                "You did not provide a date or time.\n" +
                "    Please format your input as: deadline <task name> /by <date/time>."
                );
        }
        this.time = time;
    }

    /**
     * Displays task with the status of its completion and date/time
     * 
     * @return taskName with completed status and date/time
     */
    @Override
    public String listTask() {
        return "[D]" + super.listTask() + " (by: " + this.time + ")";
    }
}