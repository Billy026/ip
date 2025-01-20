import java.util.ArrayList;
import java.util.Scanner;

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

            if (input[0].equals("list")) {
                // Lists current commands
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
                // Echoes and stores command
                String cmd = String.join(" ", input);
                commands.add(new Task(cmd));

                System.out.println("    You added \"" + cmd + "\" to your list");
            }

            System.out.println("    ___________________________________");
            System.out.println();
        }

        System.out.println("    Bye! See you soon!");
        System.out.println("    ___________________________________");
    }
}
