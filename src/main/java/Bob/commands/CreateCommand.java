package bob.commands;

import bob.exceptions.InvalidCommandException;
import bob.exceptions.InvalidDateFormatException;
import bob.exceptions.InvalidTaskOperationException;
import bob.managers.DateManager;
import bob.managers.TaskManager;
import bob.tasks.Task;

/**
 * User command to create a task.
 * Type of task is determined by inputted taskType {T, D, E}.
 */
public class CreateCommand extends Command {
    private static final String bySeparator = "/by";
    private static final String fromSeparator = "/from";
    private static final String toSeparator = "/to";
    private static final String deadlineShortForm = "D";
    private static final String eventShortForm = "E";

    private String taskType;
    private String errorMessage;

    /**
     * Primary constructor for CreateCommand.
     *
     * @param inputs user command separated by spaces.
     * @param taskType type of task.
     * @param errorMessage specialised error message for each taskType.
     */
    public CreateCommand(String[] inputs, String taskType, String errorMessage) {
        super(inputs);
        this.taskType = taskType;
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a task based on the task type and input.
     *
     * @param taskManager the list of tasks and their operations.
     * @return newly added task.
     * @throws InvalidCommandException if not enough values are given for the task type.
     */
    @Override
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        try {
            String[] taskValues = formatInput();
            Task task = taskManager.addTask(taskType, taskValues);
            assert task != null : "task should not be null.";

            return "Sure. I've added this task:\n"
                    + task.toString() + "\n"
                    + "Now you have " + taskManager.getSize() + " task"
                    + ((taskManager.getSize() == 1) ? "" : "s") + " in the list.\n";
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidCommandException(errorMessage);
        }
    }

    /**
     * Converts user input into relevant parts for createTask().
     *
     * @return array of task values.
     * @throws InvalidTaskOperationException when no date(s) given.
     * @throws InvalidDateFormatException when invalid date format given.
     */
    private String[] formatInput() throws InvalidTaskOperationException, InvalidDateFormatException {
        String[] inputParts = splitInput();
        checkForMissingDate(inputParts);
        inputParts = convertToCorrectFormat(inputParts);
        DateManager.checkForValidEventDates(inputParts, taskType.equals(eventShortForm));

        return inputParts;
    }

    /**
     * Splits user input into task name, start date and end date.
     *
     * @return array of task name, start date and end date.
     */
    private String[] splitInput() throws InvalidTaskOperationException {
        enum ChangeValue {
            ATNAME,
            ATSTART,
            ATEND
        }

        StringBuffer name = new StringBuffer();
        StringBuffer start = new StringBuffer();
        StringBuffer end = new StringBuffer();

        ChangeValue changeValue = ChangeValue.ATNAME;
        boolean hasSpace = false;

        for (int i = 1; i < this.inputs.length; i++) {
            if (this.inputs[i].equals(bySeparator)) {
                changeValue = ChangeValue.ATSTART;
                hasSpace = false;
                continue;
            } else if (this.inputs[i].equals(fromSeparator)) {
                changeValue = ChangeValue.ATSTART;
                hasSpace = false;
                continue;
            } else if (this.inputs[i].equals(toSeparator)) {
                changeValue = ChangeValue.ATEND;
                hasSpace = false;
                continue;
            } else if (changeValue == ChangeValue.ATNAME) {
                name.append((hasSpace ? " " : "") + this.inputs[i]);
            } else if (changeValue == ChangeValue.ATSTART) {
                start.append((hasSpace ? " " : "") + this.inputs[i]);
            } else if (changeValue == ChangeValue.ATEND) {
                end.append((hasSpace ? " " : "") + this.inputs[i]);
            } else {
                continue;
            }

            if (!hasSpace) {
                hasSpace = true;
            }
        }

        if (name.toString().equals("")) {
            throw new InvalidTaskOperationException("Please provide a task name.");
        }

        return new String[] {name.toString(), start.toString(), end.toString()};
    }

    /**
     * Checks if no date is provided or date is in invalid format.
     *
     * @param inputParts array of task name, start date and end date.
     * @throws InvalidTaskOperationException if no date is provided or invalid format.
     */
    private void checkForMissingDate(String[] inputParts) throws InvalidTaskOperationException {
        boolean isDeadline = this.taskType.equals(deadlineShortForm);
        boolean isEvent = this.taskType.equals(eventShortForm);
        boolean isStartEmpty = inputParts[1].equals("");
        boolean areDatesEmpty = inputParts[1].equals("") || inputParts[2].equals("");
        boolean isByUsed = isByUsed();

        if (isDeadline && isStartEmpty) {
            throw new InvalidTaskOperationException(
                    "You did not provide a date or time.\n"
                    + "    Please format your input as: deadline <task name> /by <date with time>.");
        } else if (isEvent && (areDatesEmpty || isByUsed)) {
            throw new InvalidTaskOperationException(
                    "You did not provide either a start date or an end date.\n"
                    + "    Please format your input as: event <task name> /from <date with time> "
                    + "/to <date with time>.");
        }
    }

    /**
     * Checks if /by is in user input.
     *
     * @return /by is in user input.
     */
    private boolean isByUsed() {
        for (String input : inputs) {
            if (input.equals(bySeparator)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Combines task name, start date and end date to a standardized format.
     *
     * @param inputParts array of task name, start date and end date.
     * @return task name with start and end dates in standardized format.
     * @throws InvalidDateFormatException if date format is invalid.
     */
    private String[] convertToCorrectFormat(String[] inputParts) throws InvalidDateFormatException {
        String start = inputParts[1];
        String end = inputParts[2];

        if (!start.equals("")) {
            start = DateManager.normaliseDateFormat(start);
            if (!end.equals("")) {
                end = DateManager.normaliseDateFormat(end);
            }
        }

        return new String[] {inputParts[0], start, end};
    }
}
