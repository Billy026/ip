import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main interface for IP.
 */
public class Bob {
    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Task> tasks = new ArrayList<>();

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
        // Repeatedly executes commands until user exits
        while(true) {
            String[] input = sc.nextLine().split(" ");
            lineBreak();
            if (input[0].equals("bye")) {
                break;
            }

            try {
                executeCommand(input);
            } catch (InvalidCommandException e) {
                System.out.println("    " + e.getMessage());
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
     * @throws InvalidCommandException When an invalid command has been inputted.
     */
    private static void executeCommand(String[] input) throws InvalidCommandException {
        if (input[0].equals("todo")) {
            try {
                InputManager.createTask("T", input, tasks);
            } catch (InvalidCommandException e) {
                System.out.println("    " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException("Please give a name to the ToDo task.");
            }

        } else if (input[0].equals("deadline")) {
            try {
                InputManager.createTask("D", input, tasks);
            } catch (InvalidCommandException e) {
                System.out.println("    " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException(
                    "You did not provide a date or time.\n" +
                    "    Please format your input as: deadline <task name> /by <date/time>."
                );
            }

        } else if (input[0].equals("event")) {
            try {
                InputManager.createTask("E", input, tasks);
            } catch (InvalidCommandException e) {
                System.out.println("    " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException(
                    "You did not provide either a start date/time or an end date/time.\n" +
                    "    Please format your input as: event <task name> /from <date/time> /to <date/time>."
                );
            }

        } else if (input[0].equals("delete")) {
            try {
                if (!Character.isDigit(input[1].charAt(0))) {
                    throw new InvalidCommandException("Please provide a valid task number.");
                }
                InputManager.deleteTask(input[1].charAt(0), tasks);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException("Please indicate which task to delete.");
            }

        } else if (input[0].equals("list")) {
            InputManager.listTasks(tasks);

        } else if (input[0].equals("mark")) {
            try {
                if (!Character.isDigit(input[1].charAt(0))) {
                    throw new InvalidCommandException("Please provide a valid task number.");
                }
                InputManager.markTask(input, tasks);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException("Please indicate which task to mark.");
            }

        } else if (input[0].equals("unmark")) {
            try {
                if (!Character.isDigit(input[1].charAt(0))) {
                    throw new InvalidCommandException("Please provide a valid task number.");
                }
                InputManager.unmarkTask(input, tasks);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException("Please indicate which task to unmark.");
            }

        } else {
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