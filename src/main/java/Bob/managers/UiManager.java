package Bob.managers;

import java.util.Scanner;

import Bob.exceptions.InvalidCommandException;
import Bob.parser.Parser;

/**
 * Manages all functions related to the UI.
 * 
 * @param FILE_PATH path of file to save to.
 * @param scanner method of getting user input.
 * @param parser class that makes sense of user input.
 */
public class UiManager {
    // File path to save in hard disk
    private static String FILE_PATH = "./data/bob.txt";
    private static Scanner scanner = new Scanner(System.in);
    private Parser parser;

    /**
     * Controls the main flow of the program.
     */
    public void executeUi() {
        greet();
        this.parser = new Parser(FILE_PATH);
        executeUserCommands();
    }

    /**
     * Displays a greeting on launch of main activity.
     */
    private void greet() {
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
        printLineBreak();
        System.out.println();

        // Initial greeting
        System.out.println(
                "    Hi, I'm Bob!\n" + 
                "    Can I do something for you?");
        printLineBreak();
        System.out.println();
    }

    /**
     * Repeatedly executes user commands until user exits.
     */
    private void executeUserCommands() {
        System.out.println();
        this.parser.displayIncomingDeadlines();
        System.out.println();

        // Repeatedly executes commands until user exits
        while(true) {
            String[] input = scanner.nextLine().split(" ");
            printLineBreak();
            if (input[0].equals("bye")) {
                break;
            }

            try {
                this.parser.parseCommand(input);
            } catch (InvalidCommandException e) {
                System.err.println("    " + e.getMessage());
            }

            printLineBreak();
            System.out.println();
        }

        // Clean up
        System.out.println("    Bye! See you soon!");
        printLineBreak();
        scanner.close();
    }

    /**
     * Prints a line break.
     */
    private void printLineBreak() {
        System.out.println(
            "    __________________________________________________________________________________");
    }
}
