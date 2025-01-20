/**
 * Tasks with two dates/times
 */
public class Event extends Task {
    private String start;
    private String end;

    public Event(String taskName, String start, String end) {
        super(taskName);
        this.start = start;
        this.end = end;
    }

    /**
     * Displays task with the status of its completion, start and end dates/times
     * 
     * @return taskName with completed status, start and end dates/times
     */
    @Override
    public String listTask() {
        return "[E]" + super.listTask() + " (from: " + this.start + " to: " + this.end + ")";
    }
}