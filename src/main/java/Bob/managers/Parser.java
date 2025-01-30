package Bob.managers;

import Bob.exceptions.InvalidCommandException;
import Bob.exceptions.InvalidDateFormatException;
import Bob.exceptions.InvalidTaskOperationException;
import Bob.tasks.Task;

/**
 * Deals with making sense of the user command.
 * 
 * @param taskManager handles operations on the task list.
 */
public class Parser {
    private TaskManager taskManager;

    /**
     * Primary constructor.
     */
    public Parser() {
        this.taskManager = new TaskManager();
    }

    /**
     * Creates a task based on the task type and input.
     * 
     * @param taskType type of task.
     * @param input User input split by spaces.
     * @throws InvalidCommandException if invalid task type given.
     */
    public void createTask(String taskType, String[] input) throws InvalidCommandException {
        try {
            String[] values = splitInput(input, taskType);
            Task task = this.taskManager.addTask(taskType, values);

            System.out.println(
                    "    Sure. I've added this task:\n" +
                    "      " + task.toString() + "\n" +
                    "    Now you have " + this.taskManager.getSize() + " task" +
                    ((this.taskManager.getSize() == 1) ? "" : "s") + " in the list.");
        } catch (InvalidTaskOperationException e) {
            System.err.println("    " + e.getMessage());
        } catch (InvalidDateFormatException e) {
            System.err.println("    " + e.getMessage());
        }
    }

    /**
     * Splits the input into relevant parts for createTask().
     * 
     * @param input user input.
     * @param taskType type of task.
     * @return array of relevant Strings.
     * @throws InvalidTaskOperationException when no date(s) given.
     * @throws InvalidDateFormatException when invalid date format given.
     */
    private String[] splitInput(String[] input, String taskType)
            throws InvalidTaskOperationException, InvalidDateFormatException {
        // Builds the different parts of the output
        StringBuffer name = new StringBuffer();
        StringBuffer start = new StringBuffer();
        StringBuffer end = new StringBuffer();

        // Values to determine new parts of output
        int change = 0;
        boolean hasSpace = false;
        boolean isWrongEventSyntax = false;

        // Convert input to relevant parts
        for (int i = 1; i < input.length; i++) {
            // Check for special syntaxes in input
            if (input[i].equals("/by")) {
                change = 1;
                isWrongEventSyntax = true;
                hasSpace = false;
                continue;
            } else if (input[i].equals("/from")) {
                change = 1;
                hasSpace = false;
                continue;
            }
            if (input[i].equals("/to")) {
                change = 2;
                hasSpace = false;
                continue;
            }

            ((change == 0) ? name : (change == 1) ? start : end).append(
                    ((hasSpace) ? " " : "") + input[i]);
            if (!hasSpace) hasSpace = true;
        }

        String taskName = name.toString();
        String startDate = start.toString();
        String endDate = end.toString();

        // Check for missing date
        if (taskType.equals("D") && startDate.equals("")) { // Check if date provided
            throw new InvalidTaskOperationException(
                    "You did not provide a date or time.\n" +
                    "    Please format your input as: deadline <task name> /by <date>.");
        } else if (taskType.equals("E") &&
                (((startDate.equals("") || endDate.equals(""))) || // Check if dates are provided
                isWrongEventSyntax)) {   // Check if /by is used instead of /from and /to
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

    /**
     * Deletes a task from the list of tasks.
     * 
     * @param c char to transform into task number to delete.
     * @throws InvalidCommandException when invalid task number given.
     */
    public void deleteTask(char c) throws InvalidCommandException {
        // Convert task number to int
        int num = c - '0';
        if (this.taskManager.getSize() < num) {
            throw new InvalidCommandException("There is no task with that number.");
        }

        // Delete task
        Task task = this.taskManager.getTask(num - 1);
        System.out.println(
                "    Alright. I've removed this task:\n" +
                "      " + task.toString());
        this.taskManager.deleteTask(num - 1);

        System.out.println(
                "    Now you have " + this.taskManager.getSize() + " task" +
                ((this.taskManager.getSize() == 1) ? "" : "s") + " in the list.");
    }

    /**
     * Displays all tasks and their status as a numbered list.
     */
    public void listTasks() {
        if (this.taskManager.getSize() != 0) {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 1; i <= this.taskManager.getSize(); i++) {
                System.out.println("    " + i + ". " + this.taskManager.getTask(i - 1).toString());
            }
        } else {
            System.out.println("    There are currently no tasks in your list.");
        }
    }

    /**
     * Marks a task.
     * 
     * @param input user input converted to an array.
     * @throws InvalidCommandException when invalid task number given.
     */
    public void markTask(String[] input) throws InvalidCommandException {
        try {
            // Convert task number to int
            int num = input[1].charAt(0) - '0';
            if (this.taskManager.getSize() < num) {
                throw new InvalidCommandException("There is no task with that number.");
            }

            Task task = this.taskManager.markTask(num - 1, true);

            System.out.println(
                    "    Nice! I've marked this task as done:\n" +
                    "      " + task.toString());
        } catch (InvalidTaskOperationException e) {
            System.err.println("    " + e.getMessage());
        }
    }

    /**
     * Unmarks a task.
     * 
     * @param input user input converted to an array.
     * @throws InvalidCommandException when invalid task number given.
     */
    public void unmarkTask(String[] input) throws InvalidCommandException {
        try {
            // Convert task number to int
            int num = input[1].charAt(0) - '0';
            if (this.taskManager.getSize() < num) {
                throw new InvalidCommandException("There is no task with that number.");
            }

            Task task = this.taskManager.markTask(num - 1, false);

            System.out.println(
                    "    Oh, I guess it's not done yet:\n" +
                    "      " + task.toString());
        } catch (InvalidTaskOperationException e) {
            System.err.println("    " + e.getMessage());
        }
    }

    /**
     * Propogates displayIncomingDeadlines to taskManager.
     */
    public void displayIncomingDeadlines() {
        this.taskManager.displayIncomingDeadlines();
    }
}