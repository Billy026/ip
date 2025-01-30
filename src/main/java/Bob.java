import java.util.Scanner;

import exceptions.InvalidCommandException;
import managers.TaskManager;

/**
 * Main interface for IP.
 */
public class Bob {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        greeting();
        storeAndList();

        // Clean up
        sc.close();
    }

    /**
     * Displays a greeting on launch of main activity.
     */
    private static void greeting() {
        // Printing of logo
        String logo = 
                  "      ____        _        \n"
                + "     |  _ \\      | |      \n"
                + "     | |_| |     | |       \n"
                + "     |    /  ___ | | __    \n"
                + "     |  _ \\ / _ \\| |/_ \\\n"
                + "     | |_| | |_| |  |_| |  \n"
                + "     |____/ \\___/|_|\\__/ \n";
        System.out.println("    Hello from\n" + logo);
        lineBreak();
        System.out.println();

        // Initial greeting
        System.out.println(
                "    Hi, I'm Bob!\n" + 
                "    Can I do something for you?");
        lineBreak();
        System.out.println();
    }

    /**
     * Handles execution of commands.
     */
    private static void storeAndList() {
        TaskManager taskManager = new TaskManager();
        System.out.println();
        taskManager.displayIncomingDeadlines();
        System.out.println();

        // Repeatedly executes commands until user exits
        while(true) {
            String[] input = sc.nextLine().split(" ");
            lineBreak();
            if (input[0].equals("bye")) {
                break;
            }

            try {
                executeCommand(input, taskManager);
            } catch (InvalidCommandException e) {
                System.err.println("    " + e.getMessage());
            }

            lineBreak();
            System.out.println();
        }

        System.out.println("    Bye! See you soon!");
        lineBreak();
    }

    /**
     * Handles logic of inputted commands.
     * 
     * @param input user input converted to an array.
     * @param taskManager TaskManager object to handle tasks.
     * @throws InvalidCommandException When an invalid command has been inputted.
     */
    private static void executeCommand(String[] input, TaskManager taskManager) throws InvalidCommandException {
        switch (input[0]) {
            case "todo":
                try {
                    taskManager.createTask("T", input);
                } catch (InvalidCommandException e) {
                    System.err.println("    " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException("Please give a name to the ToDo task.");
                }
                break;
            case "deadline":
                try {
                    taskManager.createTask("D", input);
                } catch (InvalidCommandException e) {
                    System.err.println("    " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException(
                        "You did not provide a date or time.\n" +
                        "    Please format your input as: deadline <task name> /by <date>."
                    );
                }
                break;
            case "event":
                try {
                    taskManager.createTask("E", input);
                } catch (InvalidCommandException e) {
                    System.err.println("    " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException(
                        "You did not provide either a start date or an end date.\n" +
                        "    Please format your input as: event <task name> /from <date> /to <date>."
                    );
                }
                break;
            case "delete":
                try {
                    if (!Character.isDigit(input[1].charAt(0))) {
                        throw new InvalidCommandException("Please provide a valid task number.");
                    }
                    taskManager.deleteTask(input[1].charAt(0));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException("Please indicate which task to delete.");
                }
                break;
            case "list":
                taskManager.listTasks();
                break;
            case "mark":
                try {
                    if (!Character.isDigit(input[1].charAt(0))) {
                        throw new InvalidCommandException("Please provide a valid task number.");
                    }
                    taskManager.markTask(input);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException("Please indicate which task to mark.");
                }
                break;
            case "unmark":
                try {
                    if (!Character.isDigit(input[1].charAt(0))) {
                        throw new InvalidCommandException("Please provide a valid task number.");
                    }
                    taskManager.unmarkTask(input);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException("Please indicate which task to unmark.");
                }
                break;
            default:
                throw new InvalidCommandException("I don't understand.");
        }
    }

    /**
     * Prints a line break.
     */
    private static void lineBreak() {
        System.out.println(
            "    __________________________________________________________________________________");
    }
}