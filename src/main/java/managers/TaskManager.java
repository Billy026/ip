package managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidCommandException;
import exceptions.InvalidDateFormatException;
import exceptions.InvalidTaskOperationException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

/**
 * Contains the different operations related to the display of tasks.
 */
public class TaskManager {
    // File path for saving tasks
    private static final String FILE_PATH = "./data/bob.txt";
    private List<Task> tasks;

    /**
     * Primary constructor.
     */
    public TaskManager() {
        this.tasks = new ArrayList<>();
        loadTasks();
    }

    // Functions involving the hard disk

    /**
     * Saves a task to data file.
     * 
     * @param newTask task to save.
     */
    private void saveTask(Task newTask) {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs(); // Ensures parent directory exists

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(newTask.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("    There was a problem saving the task: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from data file into task list.
     */
    private void loadTasks() {
        File file = new File(FILE_PATH);
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    tasks.add(Task.fromSaveFormat(line));
                } catch (Exception e) { // Handle corrupted task
                    System.err.println("    There was a problem loading the task: " + e.getMessage());
                }
            }
            System.out.println("    Saved task list found.");
        } catch (FileNotFoundException e) {
            System.out.println("    No saved task list found.");
        } catch (IOException e) {
            System.err.println("    There was a problem loading the file: " + e.getMessage());
        }
    }

    /**
     * Rewrites the task list to the data file.
     */
    private void rewriteTaskList() {
        File file = new File(FILE_PATH);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : this.tasks) {
                writer.write(task.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("    There was a problem saving the task: " + e.getMessage());
        }
    }

    // Functions involving the user input

    /**
     * Creates a task based on the task type and input.
     * 
     * @param taskType type of task.
     * @param input User input split by spaces.
     * @throws InvalidCommandException If invalid task type given.
     */
    public void createTask(String taskType, String[] input) throws InvalidCommandException {
        try {
            String[] values = splitInput(input, taskType);

            Task task = null;

            // Create relevant task based on task type
            if (taskType.equals("T")) {
                task = new ToDo(values[0]);
                this.tasks.add(task);
            } else if (taskType.equals("D")) {
                task = new Deadline(values[0], values[1]);
                this.tasks.add(task);
            } else if (taskType.equals("E")) {
                task = new Event(values[0], values[1], values[2]);
                this.tasks.add(task);
            } else {
                throw new InvalidCommandException(
                    "Invalid task type. The valid task types are: T, D, E.");
            }

            saveTask(task);

            System.out.println(
                    "    Sure. I've added this task:\n" +
                    "      " + task.toString() + "\n" +
                    "    Now you have " + this.tasks.size() + " task" +
                    ((this.tasks.size() == 1) ? "" : "s") + " in the list.");
        } catch (InvalidTaskOperationException e) {
            // Invalid formatting
            System.err.println("    " + e.getMessage());
        } catch (InvalidDateFormatException e) {
            System.err.println("    " + e.getMessage());
        }
    }

    /**
     * Splits the input into relevant parts for createTask().
     * 
     * @param input user input.
     * @return array of relevant Strings.
     * @throws InvalidTaskOperationException When invalid date/time given.
     * @throws InvalidDateFormatException When invalid date format given.
     */
    private String[] splitInput(String[] input, String taskType)
            throws InvalidTaskOperationException, InvalidDateFormatException {
        StringBuffer name = new StringBuffer();
        StringBuffer start = new StringBuffer();
        StringBuffer end = new StringBuffer();
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

            ((change == 0) ? name : (Math.abs(change) == 1) ? start : end).append(
                ((hasSpace) ? " " : "") + input[i]);
            if (!hasSpace) hasSpace = true;
        }

        String taskName = name.toString();
        String startDate = start.toString();
        String endDate = end.toString();

        // Check for missing date/time
        if (taskType.equals("D") && startDate.equals("")) {
            throw new InvalidTaskOperationException(
                    "You did not provide a date or time.\n" +
                    "    Please format your input as: deadline <task name> /by <date/time>.");
        } else if (taskType.equals("E") &&
                ((startDate.equals("") || endDate.equals(""))) ||
                isWrongEventSyntax) {
            throw new InvalidTaskOperationException(
                    "You did not provide either a start date/time or an end date/time.\n" +
                    "    Please format your input as: event <task name> /from <date/time> /to <date/time>.");
        }

        // Convert date/time to correct format
        try {
            String format = DateManager.detectDateFormat(startDate);

            if (format.contains("ddd hh")) {
                startDate = DateManager.convertDateToFormat(startDate, "ddd hh:mm");
                endDate = DateManager.convertDateToFormat(endDate, "ddd hh:mm");
            } else if (format.contains("hh")) {
                if (format.contains("MMM")) {
                    startDate = DateManager.convertDateToFormat(startDate, "hh:mm dd MMMM yyyy");
                    endDate = DateManager.convertDateToFormat(endDate, "hh:mm dd MMMM yyyy");
                } else {
                    startDate = DateManager.convertDateToFormat(startDate, "hh:mm dd-MM-yyyy");
                    endDate = DateManager.convertDateToFormat(endDate, "hh:mm dd-MM-yyyy");
                }
            } else {
                if (format.contains("MMM")) {
                    startDate = DateManager.convertDateToFormat(startDate, "dd MMMM yyyy");
                    endDate = DateManager.convertDateToFormat(endDate, "dd MMMM yyyy");
                } else {
                    startDate = DateManager.convertDateToFormat(startDate, "dd/MM/yyyy");
                    endDate = DateManager.convertDateToFormat(endDate, "dd/MM/yyyy");
                }
            }

            return new String[] {taskName, startDate, endDate};
        } catch (InvalidDateFormatException e) {
            throw e;
        }
    }

    /**
     * Deletes a task from the list of tasks.
     * 
     * @param c char to transform into task number to delete.
     * @throws InvalidCommandException When invalid task number given.
     */
    public void deleteTask(char c) throws InvalidCommandException {
        // Convert task number to int
        int num = c - '0';
        if (this.tasks.size() < num) {
            throw new InvalidCommandException("There is no task with that number.");
        }

        // Delete task
        Task task = this.tasks.get(num - 1);
        System.out.println(
                "    Alright. I've removed this task:\n" +
                "      " + task.toString());
        this.tasks.remove(num - 1);

        rewriteTaskList();

        System.out.println(
                "    Now you have " + this.tasks.size() + " task" +
                ((this.tasks.size() == 1) ? "" : "s") + " in the list.");
    }

    /**
     * Displays all tasks and their status as a numbered list.
     */
    public void listTasks() {
        if (this.tasks.size() != 0) {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 1; i <= this.tasks.size(); i++) {
                System.out.println("    " + i + ". " + this.tasks.get(i - 1).toString());
            }
        } else {
            System.out.println("    There are currently no tasks in your list.");
        }
    }

    /**
     * Marks a task.
     * 
     * @param input user input converted to an array.
     * @throws InvalidCommandException When invalid task number given.
     */
    public void markTask(String[] input) throws InvalidCommandException {
        try {
            // Convert task number to int
            int num = input[1].charAt(0) - '0';
            if (this.tasks.size() < num) {
                throw new InvalidCommandException("There is no task with that number.");
            }

            Task task = this.tasks.get(num - 1);
            task.check();

            rewriteTaskList();

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
     * @throws InvalidCommandException When invalid task number given.
     */
    public void unmarkTask(String[] input) throws InvalidCommandException {
        try {
            // Convert task number to int
            int num = input[1].charAt(0) - '0';
            if (this.tasks.size() < num) {
                throw new InvalidCommandException("There is no task with that number.");
            }

            Task task = this.tasks.get(num - 1);
            task.uncheck();

            rewriteTaskList();

            System.out.println(
                    "    Oh, I guess it's not done yet:\n" +
                    "      " + task.toString());
        } catch (InvalidTaskOperationException e) {
            System.err.println("    " + e.getMessage());
        }
    }
}