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
 * 
 * @param taskType type of task.
 * @param errorMessage specialised error message for each taskType.
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
     * @param inputs user commanded separated by spaces.
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
    public String exec(TaskManager taskManager) throws InvalidCommandException {
        try {
            String[] taskValues = formatInput();
            Task task = taskManager.addTask(taskType, taskValues);

            return "Sure. I've added this task:\n" +
                    task.toString() + "\n" +
                    "Now you have " + taskManager.getSize() + " task" +
                    ((taskManager.getSize() == 1) ? "" : "s") + " in the list.\n";
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidCommandException(errorMessage);
        }
    }

    /**
     * Splits the input into relevant parts for createTask().
     * 
     * @return array of task values.
     * @throws InvalidTaskOperationException when no date(s) given.
     * @throws InvalidDateFormatException when invalid date format given.
     */
    private String[] formatInput() throws InvalidTaskOperationException, InvalidDateFormatException {
        String[] inputParts = splitInput();
        checkForMissingDate(inputParts);
        inputParts = convertToCorrectFormat(inputParts);
        
        return inputParts;
    }

    private String[] splitInput() {
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
            } else if (this.inputs[i].equals(fromSeparator)) {
                changeValue = ChangeValue.ATSTART;
                hasSpace = false;
            } else if (this.inputs[i].equals(toSeparator)) {
                changeValue = ChangeValue.ATEND;
                hasSpace = false;
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

        return new String[] {name.toString(), start.toString(), end.toString()};
    }

    private void checkForMissingDate(String[] inputParts) throws InvalidTaskOperationException {
        boolean isDeadline = this.taskType.equals(deadlineShortForm);
        boolean isEvent = this.taskType.equals(eventShortForm);
        boolean isStartEmpty = inputParts[1].equals("");
        boolean areDatesEmpty = inputParts[1].equals("") || inputParts[2].equals("");
        boolean isToUsed = isToUsed();

        if (isDeadline && isStartEmpty) {
            throw new InvalidTaskOperationException(
                    "You did not provide a date or time.\n" +
                    "    Please format your input as: deadline <task name> /by <date>.");
        } else if (isEvent && (areDatesEmpty || isToUsed)) {
            throw new InvalidTaskOperationException(
                    "You did not provide either a start date or an end date.\n" +
                    "    Please format your input as: event <task name> /from <date> /to <date>.");
        }
    }

    private boolean isToUsed() {
        for (String input : inputs) {
            if (input.equals(toSeparator)) {
                return true;
            }
        }

        return false;
    }

    private String[] convertToCorrectFormat(String[] inputParts) throws InvalidDateFormatException {
        String start = inputParts[1];
        String end = inputParts[2];

        if (start != "") {
            start = DateManager.normaliseDateFormat(start);
            if (end != "") {
                end = DateManager.normaliseDateFormat(end);
            }
        }

        return new String[] {inputParts[0], start, end};
    }
}
