package Bob.managers;

import java.util.Scanner;

import Bob.exceptions.InvalidCommandException;

/**
 * Manages all functions related to the UI.
 * 
 * @param parser class that makes sense of user input.
 */
public class UiManager {
    private Parser parser;

    /**
     * Primary constructor.
     */
    public UiManager() {
        this.parser = new Parser();
    }

    /**
     * Controls the main flow of the program.
     * 
     * @param sc scanner to receive user input.
     */
    public void executeUi(Scanner sc) {
        greeting();
        storeAndList(sc);
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
    private void storeAndList(Scanner sc) {
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
                executeCommand(input);
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
     * Propogates the relevant command to the parser.
     * 
     * @param input user input converted to an array.
     * @throws InvalidCommandException when an invalid command has been inputted.
     */
    private void executeCommand(String[] input) throws InvalidCommandException {
        switch (input[0]) {
            case "todo":
                try {
                    this.parser.createTask("T", input);
                } catch (InvalidCommandException e) {
                    System.err.println("    " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException("Please give a name to the ToDo task.");
                }
                break;
            case "deadline":
                try {
                    this.parser.createTask("D", input);
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
                    this.parser.createTask("E", input);
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
                    this.parser.deleteTask(input[1].charAt(0));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException("Please indicate which task to delete.");
                }
                break;
            case "list":
                this.parser.listTasks();
                break;
            case "mark":
                try {
                    if (!Character.isDigit(input[1].charAt(0))) {
                        throw new InvalidCommandException("Please provide a valid task number.");
                    }
                    this.parser.markTask(input);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new InvalidCommandException("Please indicate which task to mark.");
                }
                break;
            case "unmark":
                try {
                    if (!Character.isDigit(input[1].charAt(0))) {
                        throw new InvalidCommandException("Please provide a valid task number.");
                    }
                    this.parser.unmarkTask(input);
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
    private void lineBreak() {
        System.out.println(
            "    __________________________________________________________________________________");
    }
}