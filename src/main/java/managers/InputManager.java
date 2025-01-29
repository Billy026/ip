package managers;

import java.lang.StringBuffer;
import java.util.ArrayList;

import exceptions.InvalidCommandException;
import exceptions.InvalidTaskOperationException;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

/**
 * Contains the different operations that can be performed on the list of tasks.
 */
public abstract class InputManager {
    /**
     * Creates a task based on the task type and input.
     * 
     * @param taskType type of task.
     * @param input User input split by spaces.
     * @param tasks list of tasks.
     * @throws InvalidCommandException If invalid task type given.
     */
    public static void createTask(String taskType, String[] input, ArrayList<Task> tasks)
            throws InvalidCommandException {
        String[] values = splitInput(input);

        try {
            Task task = null;

            // Create relevant task based on task type
            if (taskType.equals("T")) {
                task = new ToDo(values[0]);
                tasks.add(task);
            } else if (taskType.equals("D")) {
                task = new Deadline(values[0], values[1]);
                tasks.add(task);
            } else if (taskType.equals("E")) {
                task = new Event(values[0], values[1], values[2]);
                tasks.add(task);
            } else {
                throw new InvalidCommandException(
                    "Invalid task type. The valid task types are: T, D, E.");
            }

            System.out.println(
                    "    Sure. I've added this task:\n" +
                    "      " + task.toString() + "\n" +
                    "    Now you have " + tasks.size() + " task" +
                    ((tasks.size() == 1) ? "" : "s") + " in the list.");
        } catch (InvalidTaskOperationException e) {
            // Invalid formatting
            System.out.println("    " + e.getMessage());
        }
    }

    /**
     * Splits the input into relevant parts for createTask().
     * 
     * @param input user input.
     * @return array of relevant Strings.
     */
    private static String[] splitInput(String[] input) {
        StringBuffer name = new StringBuffer();
        StringBuffer start = new StringBuffer();
        StringBuffer end = new StringBuffer();
        int change = 0;
        boolean space = false;

        for (int i = 1; i < input.length; i++) {
            // Check for special syntaxes in input
            if (input[i].equals("/by") || input[i].equals("/from")) {
                change = 1;
                space = false;
                continue;
            }
            if (input[i].equals("/to")) {
                change = 2;
                space = false;
                continue;
            }

            ((change == 0) ? name : (change == 1) ? start : end).append(((space) ? " " : "") + input[i]);
            if (!space) space = true;
        }

        return new String[] {name.toString(), start.toString(), end.toString()};
    }

    /**
     * Deletes a task from the list of tasks.
     * 
     * @param c char to transform into task number to delete.
     * @param tasks list of tasks.
     * @throws InvalidCommandException When invalid task number given.
     */
    public static void deleteTask(char c, ArrayList<Task> tasks) throws InvalidCommandException {
        // Convert task number to int
        int num = c - '0';
        if (tasks.size() < num) {
            throw new InvalidCommandException("There is no task with that number.");
        }

        // Delete task
        Task task = tasks.get(num - 1);
        System.out.println(
                "    Alright. I've removed this task:\n" +
                "      " + task.toString());
        tasks.remove(num - 1);

        System.out.println(
                "    Now you have " + tasks.size() + " task" +
                ((tasks.size() == 1) ? "" : "s") + " in the list.");
    }

    /**
     * Displays all tasks and their status as a numbered list.
     * 
     * @param tasks list of added tasks.
     */
    public static void listTasks(ArrayList<Task> tasks) {
        if (tasks.size() != 0) {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 1; i <= tasks.size(); i++) {
                System.out.println("    " + i + ". " + tasks.get(i - 1).toString());
            }
        } else {
            System.out.println("    There are currently no tasks in your list.");
        }
    }

    /**
     * Marks a task.
     * 
     * @param input user input converted to an array.
     * @param tasks list of added tasks.
     * @throws InvalidCommandException When invalid task number given.
     */
    public static void markTask(String[] input, ArrayList<Task> tasks) throws InvalidCommandException {
        try {
            // Convert task number to int
            int num = input[1].charAt(0) - '0';
            if (tasks.size() < num) {
                throw new InvalidCommandException("There is no task with that number.");
            }

            Task task = tasks.get(num - 1);
            task.check();
            System.out.println(
                    "    Nice! I've marked this task as done:\n" +
                    "      " + task.toString());
        } catch (InvalidTaskOperationException e) {
            System.out.println("    " + e.getMessage());
        }
    }

    /**
     * Unmarks a task.
     * 
     * @param input user input converted to an array.
     * @param tasks list of added tasks.
     * @throws InvalidCommandException When invalid task number given.
     */
    public static void unmarkTask(String[] input, ArrayList<Task> tasks) throws InvalidCommandException {
        try {
            // Convert task number to int
            int num = input[1].charAt(0) - '0';
            if (tasks.size() < num) {
                throw new InvalidCommandException("There is no task with that number.");
            }

            Task task = tasks.get(num - 1);
            task.uncheck();
            System.out.println(
                    "    Oh, I guess it's not done yet:\n" +
                    "      " + task.toString());
        } catch (InvalidTaskOperationException e) {
            System.out.println("    " + e.getMessage());
        }
    }
}