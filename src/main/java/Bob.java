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
        System.out.println("    ___________________________________");

        // Initial greeting
        System.out.println("    Hi, I'm Bob!");
        System.out.println("    Can I do something for you?");
        System.out.println("    ___________________________________");
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
            System.out.println("    ___________________________________");

            if (input[0].equals("todo")) {
                // Stores a ToDo task
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i < input.length; i++) {
                    sb.append(input[i]);
                }
                String command = sb.toString();

                ToDo task = new ToDo(command);
                commands.add(task);

                System.out.println("    Sure. I've added this task:");
                System.out.println("      " + task.listTask());
                System.out.println("    Now you have " + commands.size() + " task" + ((commands.size() == 1) ? "" : "s") + " in the list.");
            } else if (input[0].equals("deadline")) {
                // Stores a Deadline task
                StringBuffer name = new StringBuffer();
                StringBuffer time = new StringBuffer();
                boolean change = false;

                for (int i = 1; i < input.length; i++) {
                    if (input[i].equals("/by")) {
                        change = true;
                        continue;
                    }
                    ((change) ? time : name).append(input[i]);
                }
                String command = name.toString();
                String duration = time.toString();

                Deadline task = new Deadline(command, duration);
                commands.add(task);

                System.out.println("    Sure. I've added this task:");
                System.out.println("      " + task.listTask());
                System.out.println("    Now you have " + commands.size() + " task" + ((commands.size() == 1) ? "" : "s") + " in the list.");
            } else if (input[0].equals("event")) {
                // Stores an Event task
                StringBuffer name = new StringBuffer();
                StringBuffer start = new StringBuffer();
                StringBuffer end = new StringBuffer();
                int change = 0;

                for (int i = 1; i < input.length; i++) {
                    if (input[i].equals("/from")) {
                        change = 1;
                        continue;
                    }
                    if (input[i].equals("/to")) {
                        change = 2;
                        continue;
                    }
                    ((change == 0) ? name : (change == 1) ? start : end).append(input[i]);
                }
                String command = name.toString();
                String startTime = start.toString();
                String endTime = end.toString();

                Event task = new Event(command, startTime, endTime);
                commands.add(task);

                System.out.println("    Sure. I've added this task:");
                System.out.println("      " + task.listTask());
                System.out.println("    Now you have " + commands.size() + " task" + ((commands.size() == 1) ? "" : "s") + " in the list.");
            } else if (input[0].equals("list")) {
                // Lists current commands
                System.out.println("Here are the tasks in your list:");
                for (int i = 1; i <= commands.size(); i++) {
                    System.out.println("    " + i + ". " + commands.get(i - 1).listTask());
                }
            } else if (input[0].equals("mark") && Character.isDigit(input[1].charAt(0))) {
                // Mark Task as completed
                try {
                    int num = input[1].charAt(0) - '0';
                    Task task = commands.get(num - 1);
                    task.check();
                    System.out.println("    Nice! I've marked this task as done:");
                    System.out.println("      " + task.listTask());
                } catch (InvalidTaskOperationException e) {
                    System.out.println("    " + e.getMessage());
                }
            } else if (input[0].equals("unmark") && Character.isDigit(input[1].charAt(0))) {
                // Mark Task as uncompleted
                try {
                    int num = input[1].charAt(0) - '0';
                    Task task = commands.get(num - 1);
                    task.uncheck();
                    System.out.println("    Oh, I guess it's not done yet:");
                    System.out.println("      " + task.listTask());
                } catch (InvalidTaskOperationException e) {
                    System.out.println("    " + e.getMessage());
                }
            } else if (input[0].equals("bye")) {
                // Exit function call
                break;
            } else {
                System.out.println("Sorry, that's out of my capabilities.");
            }

            System.out.println("    ___________________________________");
            System.out.println();
        }

        System.out.println("    Bye! See you soon!");
        System.out.println("    ___________________________________");
    }
}
