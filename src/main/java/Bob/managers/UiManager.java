package Bob.managers;

import java.util.Scanner;

import Bob.exceptions.InvalidCommandException;

/**
 * Manages all functions related to the UI.
 * 
 * @param parser class that makes sense of user input.
 */
public class UiManager {
    // File path to save in hard disk
    private static String FILE_PATH = "./data/bob.txt";
    private static Scanner sc = new Scanner(System.in);
    private Parser parser;

    /**
     * Controls the main flow of the program.
     * 
     * @param sc scanner to receive user input.
     */
    public void executeUi() {
        greeting();
        this.parser = new Parser(FILE_PATH);
        storeAndList();
    }

    /**
     * Displays a greeting on launch of main activity.
     */
    private void greeting() {
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
     * Repeatedly executes user commands.
     * 
     * @param sc scanner to receive user input.
     */
    private void storeAndList() {
        System.out.println();
        this.parser.displayIncomingDeadlines();
        System.out.println();

        // Repeatedly executes commands until user exits
        while(true) {
            String[] input = sc.nextLine().split(" ");
            lineBreak();
            if (input[0].equals("bye")) {
                break;
            }

            try {
                this.parser.parseCommand(input);
            } catch (InvalidCommandException e) {
                System.err.println("    " + e.getMessage());
            }

            lineBreak();
            System.out.println();
        }

        // Clean up
        System.out.println("    Bye! See you soon!");
        lineBreak();
        sc.close();
    }

    /**
     * Prints a line break.
     */
    private void lineBreak() {
        System.out.println(
            "    __________________________________________________________________________________");
    }
}
