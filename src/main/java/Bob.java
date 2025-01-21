import java.util.ArrayList;
import java.util.Scanner;
import java.lang.StringBuffer;

/**
 * Main class for IP
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
     * Displays a greeting on launch of main activity
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
        System.out.println("    __________________________________________________________________________________");

        // Initial greeting
        System.out.println("    Hi, I'm Bob!");
        System.out.println("    Can I do something for you?");
        System.out.println("    __________________________________________________________________________________");
        System.out.println();
    }

    /**
     * Allows storing and displaying of commands
     */
    private static void storeAndList() {
        ArrayList<Task> commands = new ArrayList<>();

        // Performs different operations depending on user input
        while(true) {
            String[] input = sc.nextLine().split(" ");
            System.out.println("    __________________________________________________________________________________");

            if (input[0].equals("bye")) {
                break;
            }

            try {
                executeCommand(input, commands);
            } catch (InvalidCommandException e) {
                System.out.println("    " + e.getMessage());
            }

            System.out.println("    __________________________________________________________________________________");
            System.out.println();
        }

        System.out.println("    Bye! See you soon!");
        System.out.println("    __________________________________________________________________________________");
    }

    /**
     * If statement that handles the logic of inputted commands
     * 
     * @param input user input converted to an array
     * @param commands list of added tasks
     * 
     * @throws InvalidCommandException
     * When an invalid command has been inputted.
     */
    public static void executeCommand(String[] input, ArrayList<Task> commands) throws InvalidCommandException {
        if (input[0].equals("todo")) {
            try {
                toDoTask(input, commands);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException("Please give a name to the ToDo task.");
            }
        } else if (input[0].equals("deadline")) {
            try {
                deadlineTask(input, commands);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException(
                    "You did not provide a date or time.\n" +
                    "    Please format your input as: deadline <task name> /by <date/time>."
                );
            }
        } else if (input[0].equals("event")) {
            try {
                eventTask(input, commands);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException(
                    "You did not provide either a start date/time or an end date/time.\n" +
                    "    Please format your input as: event <task name> /from <date/time> /to <date/time>."
                );
            }
        } else if (input[0].equals("list")) {
            listTasks(commands);
        } else if (input[0].equals("mark")) {
            try {
                if (!Character.isDigit(input[1].charAt(0))) {
                    throw new InvalidCommandException("Please provide a valid task number.");
                }

                markTask(input, commands);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException("Please indicate which task to mark.");
            }
        } else if (input[0].equals("unmark")) {
            try {
                if (!Character.isDigit(input[1].charAt(0))) {
                    throw new InvalidCommandException("Please provide a valid task number.");
                }

                unmarkTask(input, commands);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InvalidCommandException("Please indicate which task to unmark.");
            }
        } else {
            throw new InvalidCommandException("I don't understand.");
        }
    }

    /**
     * Stores a ToDo task in the list of Tasks
     * 
     * @param input user input converted to an array
     * @param commands list of added tasks
     */
    private static void toDoTask(String[] input, ArrayList<Task> commands) {
        // Stores a ToDo task
        StringBuffer sb = new StringBuffer();
        sb.append(input[1]);
        for (int i = 2; i < input.length; i++) {
            sb.append(" " + input[i]);
        }
        String command = sb.toString();

        ToDo task = new ToDo(command);
        commands.add(task);

        System.out.println("    Sure. I've added this task:");
        System.out.println("      " + task.listTask());
        System.out.println("    Now you have " + commands.size() + " task" + ((commands.size() == 1) ? "" : "s") + " in the list.");
    }

    /**
     * Stores a Deadline task in the list of Tasks
     * 
     * @param input user input converted to an array
     * @param commands list of added tasks
     */
    private static void deadlineTask(String[] input, ArrayList<Task> commands) {
        // Stores a Deadline task
        StringBuffer name = new StringBuffer();
        StringBuffer time = new StringBuffer();
        boolean change = false;
        boolean space = false;

        for (int i = 1; i < input.length; i++) {
            if (input[i].equals("/by")) {
                change = true;
                space = false;
                continue;
            }
            ((change) ? time : name).append(((space) ? " " : "") + input[i]);
            if (!space) space = true;
        }
        String command = name.toString();
        String duration = time.toString();

        try {
            Deadline task = new Deadline(command, duration);
            commands.add(task);

            System.out.println("    Sure. I've added this task:");
            System.out.println("      " + task.listTask());
            System.out.println("    Now you have " + commands.size() + " task" + ((commands.size() == 1) ? "" : "s") + " in the list.");
        } catch (InvalidTaskOperationException e) {
            // Invalid formatting
            System.out.println("    " + e.getMessage());
        }
    }

    /**
     * Stores a Event task in the list of tasks
     * 
     * @param input user input converted to an array
     * @param commands list of added tasks
     */
    private static void eventTask(String[] input, ArrayList<Task> commands) {
        // Stores an Event task
        StringBuffer name = new StringBuffer();
        StringBuffer start = new StringBuffer();
        StringBuffer end = new StringBuffer();
        int change = 0;
        boolean space = false;

        for (int i = 1; i < input.length; i++) {
            if (input[i].equals("/from")) {
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
        String command = name.toString();
        String startTime = start.toString();
        String endTime = end.toString();

        try {
            Event task = new Event(command, startTime, endTime);
            commands.add(task);

            System.out.println("    Sure. I've added this task:");
            System.out.println("      " + task.listTask());
            System.out.println("    Now you have " + commands.size() + " task" + ((commands.size() == 1) ? "" : "s") + " in the list.");
        } catch (InvalidTaskOperationException e) {
            // Invalid formatting
            System.out.println("    " + e.getMessage());
        }
    }

    /**
     * Displays all tasks and their status as a numbered list
     * 
     * @param commands list of added tasks
     */
    private static void listTasks(ArrayList<Task> commands) {
        // Lists current commands
        if (commands.size() != 0) {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 1; i <= commands.size(); i++) {
                System.out.println("    " + i + ". " + commands.get(i - 1).listTask());
            }
        } else {
            System.out.println("    There are currently no tasks in your list.");
        }
    }

    /**
     * Marks a task
     * 
     * @param input user input converted to an array
     * @param commands list of added tasks
     * 
     * @throws InvalidCommandException
     * When invalid task number given
     */
    private static void markTask(String[] input, ArrayList<Task> commands) throws InvalidCommandException {
        // Mark Task as completed
        try {
            int num = input[1].charAt(0) - '0';
            if (commands.size() < num) throw new InvalidCommandException("There is no task with that number.");
            Task task = commands.get(num - 1);
            task.check();
            System.out.println("    Nice! I've marked this task as done:");
            System.out.println("      " + task.listTask());
        } catch (InvalidTaskOperationException e) {
            System.out.println("    " + e.getMessage());
        }
    }

    /**
     * Unmarks a task
     * 
     * @param input user input converted to an array
     * @param commands list of added tasks
     * 
     * @throws InvalidCommandException
     * When invalid task number given
     */
    private static void unmarkTask(String[] input, ArrayList<Task> commands) throws InvalidCommandException {
        // Mark Task as uncompleted
        try {
            int num = input[1].charAt(0) - '0';
            if (commands.size() < num) throw new InvalidCommandException("There is no task with that number.");
            Task task = commands.get(num - 1);
            task.uncheck();
            System.out.println("    Oh, I guess it's not done yet:");
            System.out.println("      " + task.listTask());
        } catch (InvalidTaskOperationException e) {
            System.out.println("    " + e.getMessage());
        }
    }
}
