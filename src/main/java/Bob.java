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
        ArrayList<String> commands = new ArrayList<>();

        // Performs different operations depending on user input
        while(true) {
            String input = sc.nextLine();

            if (input.equals("list")) {
                // Lists current commands
                System.out.println("    ___________________________________");
                for (int i = 1; i <= commands.size(); i++) {
                    System.out.println("    " + i + ". " + commands.get(i - 1));
                }
                System.out.println("    ___________________________________");
                System.out.println();
            } else if (input.equals("bye")) {
                // Exit function call
                break;
            } else {
                // Echoes and stores command
                commands.add(input);

                System.out.println("    ___________________________________");
                System.out.println("    You added: " + input);
                System.out.println("    ___________________________________");
                System.out.println();
            }
        }

        System.out.println("    ___________________________________");
        System.out.println("    Bye! See you soon!");
        System.out.println("    ___________________________________");
    }
}
