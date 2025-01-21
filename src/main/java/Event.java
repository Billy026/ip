/**
 * Tasks with two dates/times
 */
public class Event extends Task {
    private String start;
    private String end;

    /**
     * Primary constructor
     * 
     * @param taskName name of task
     * @param start date/time event will start
     * @param end date/time event will end
     * 
     * @throws InvalidTaskOperationException
     * Invalid date/time given
     */
    public Event(String taskName, String start, String end)throws InvalidTaskOperationException {
        super(taskName);
        if (start.equals("") || end.equals("")) {
            throw new InvalidTaskOperationException(
                "You did not provide either a start date/time or an end date/time.\n" +
                "    Please format your input as: event <task name> /from <date/time> /to <date/time>."
                );
        }
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