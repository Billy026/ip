/**
 * Tasks with only one date/time
 */
public class Deadline extends Task {
    private String time;

    public Deadline(String taskName, String time) {
        super(taskName);
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