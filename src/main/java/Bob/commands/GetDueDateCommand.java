package bob.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bob.exceptions.InvalidCommandException;
import bob.exceptions.InvalidDateFormatException;
import bob.managers.DateManager;
import bob.managers.TaskManager;
import javafx.util.Pair;

/**
 * User command to get all tasks with deadlines that are due in the given date.
 */
public class GetDueDateCommand extends Command {
    private static final String longDateFormat = "dd MMMM yyyy HH:mm";
    private static final String shortDateFormat = "dd/MM/yyyy HH:mm";

    private static final String invalidCommandMessage = "Please provide a valid due date.";

    /**
     * Primary constructor of GetDueDateCommand.
     * 
     * @param inputs user command separated by spaces.
     */
    public GetDueDateCommand(String[] inputs) {
        super(inputs);
    }

    /**
     * Gets all tasks with deadlines that are due in the given date.
     * 
     * @param taskManager the list of tasks and their operations.
     * @return list of due tasks.
     * @throws InvalidCommandException if invalid date is given.
     */
    @Override
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        String date = getDueDate();
        Pair<LocalDateTime, Boolean> dueDate = parseDueDate(date);
        return taskManager.displaySameDeadlines(dueDate);
    }

    /**
     * Parses the due date from input.
     * 
     * @return given due date.
     */
    private String getDueDate() {
        StringBuffer buffer = new StringBuffer();
        boolean hasSpace = false;

        for (int i = 1; i < inputs.length; i++) {
            if (hasSpace) {
                buffer.append(" ");
            } else {
                hasSpace = true;
            }

            buffer.append(inputs[i]);
        }

        return buffer.toString();
    }

    /**
     * Converts inputted date into a LocalDate, as well as if time is specified.
     * 
     * @param date date to convert.
     * @return pair of converted LocalDate and if time is specified.
     * @throws InvalidCommandException if invalid date or time is given.
     */
    private Pair<LocalDateTime, Boolean> parseDueDate(String date) throws InvalidCommandException {
        if (date.equals("")) {
            throw new InvalidCommandException(invalidCommandMessage);
        }

        boolean containsDate = date.split("[ ,/\\-:]").length == 5;

        try {
            String dueDate = DateManager.normaliseDateFormat(date);

            if (dueDate.contains("/")) {
                return new Pair<>(LocalDateTime.parse(dueDate, DateTimeFormatter.ofPattern(shortDateFormat)),
                        containsDate);
            } else {
                return new Pair<>(LocalDateTime.parse(dueDate, DateTimeFormatter.ofPattern(longDateFormat)),
                        containsDate);
            }
        } catch (InvalidDateFormatException e) {
            throw new InvalidCommandException(invalidCommandMessage);
        }
    }
}
