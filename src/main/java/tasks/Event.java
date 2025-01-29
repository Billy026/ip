package tasks;

import exceptions.InvalidTaskOperationException;

/**
 * Tasks with two dates/times.
 * 
 * @param start date/time event will start.
 * @param end date/time event will end.
 */
public class Event extends Task {
    private String start;
    private String end;

    /**
     * Primary constructor.
     * 
     * @param taskName name of task.
     * @param start date/time event will start.
     * @param end date/time event will end.
     * @throws InvalidTaskOperationException Invalid date/time given.
     */
    public Event(String taskName, String start, String end)throws InvalidTaskOperationException {
        super(taskName, "E");

        // Check correct formatting of constructor command
        if (start.equals("") || end.equals("")) {
            throw new InvalidTaskOperationException(
                    "You did not provide either a start date/time or an end date/time.\n" +
                    "    Please format your input as: event <task name> /from <date/time> /to <date/time>.");
        }

        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return super.toString() + " | from: " + this.start + ", to: " + this.end;
    }
}