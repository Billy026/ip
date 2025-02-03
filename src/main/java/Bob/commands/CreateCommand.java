package Bob.commands;

import Bob.exceptions.InvalidCommandException;
import Bob.exceptions.InvalidDateFormatException;
import Bob.exceptions.InvalidTaskOperationException;
import Bob.managers.DateManager;
import Bob.managers.TaskManager;
import Bob.tasks.Task;

public class CreateCommand extends Command {
    private String taskType;
    private String errorMessage;

    public CreateCommand(String[] inputs, String taskType, String errorMessage) {
        super(inputs);
        this.taskType = taskType;
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a task based on the task type and input.
     * 
     * @param taskType type of task.
     * @param input User input split by spaces.
     * @throws InvalidCommandException if invalid task type given.
     */
    public void exec(TaskManager taskManager) throws InvalidCommandException {
        try {
            String[] values = splitInput();
            Task task = taskManager.addTask(taskType, values);

            System.out.println(
                    "    Sure. I've added this task:\n" +
                    "      " + task.toString() + "\n" +
                    "    Now you have " + taskManager.getSize() + " task" +
                    ((taskManager.getSize() == 1) ? "" : "s") + " in the list.");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidCommandException(errorMessage);
        }
    }

    /**
     * Splits the input into relevant parts for createTask().
     * 
     * @return array of relevant Strings.
     * @throws InvalidTaskOperationException when no date(s) given.
     * @throws InvalidDateFormatException when invalid date format given.
     */
    private String[] splitInput() throws InvalidTaskOperationException, InvalidDateFormatException {
        // Builds the different parts of the output
        StringBuffer name = new StringBuffer();
        StringBuffer start = new StringBuffer();
        StringBuffer end = new StringBuffer();

        // Values to determine new parts of output
        int change = 0;
        boolean hasSpace = false;
        boolean isWrongEventSyntax = false;

        // Convert input to relevant parts
        for (int i = 1; i < this.inputs.length; i++) {
            // Check for special syntaxes in input
            if (this.inputs[i].equals("/by")) {
                change = 1;
                isWrongEventSyntax = true;
                hasSpace = false;
                continue;
            } else if (this.inputs[i].equals("/from")) {
                change = 1;
                hasSpace = false;
                continue;
            }
            if (this.inputs[i].equals("/to")) {
                change = 2;
                hasSpace = false;
                continue;
            }

            ((change == 0) ? name : (change == 1) ? start : end).append(
                    ((hasSpace) ? " " : "") + this.inputs[i]);
            if (!hasSpace) hasSpace = true;
        }

        String taskName = name.toString();
        String startDate = start.toString();
        String endDate = end.toString();

        // Check for missing date
        if (this.taskType.equals("D") && startDate.equals("")) {
            // Check if date provided
            throw new InvalidTaskOperationException(
                    "You did not provide a date or time.\n" +
                    "    Please format your input as: deadline <task name> /by <date>.");
        } else if (this.taskType.equals("E") &&
                // Check if dates are provided
                (((startDate.equals("") || endDate.equals(""))) ||
                // Check if /by is used instead of /from and /to
                isWrongEventSyntax)) {
            throw new InvalidTaskOperationException(
                    "You did not provide either a start date or an end date.\n" +
                    "    Please format your input as: event <task name> /from <date> /to <date>.");
        }

        // Convert date to correct format
        if (startDate != "") {
            startDate = DateManager.normaliseDateFormat(startDate);
            if (endDate != "") {
                endDate = DateManager.normaliseDateFormat(endDate);
            }
        }
        
        return new String[] {taskName, startDate, endDate};
    }
}
