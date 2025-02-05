package Bob.managers;

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
    private Parser parser = new Parser(FILE_PATH);

    public String getIncomingDeadlines() {
        return this.parser.displayIncomingDeadlines();
    }

    public String executeUserCommand(String input) {
        String[] inputArray = input.split(" ");

        try {
            return this.parser.parseCommand(inputArray);
        } catch (InvalidCommandException e) {
            return e.getMessage() + "\n";
        }
    }

    public String getSavedListMessage() {
        return this.parser.getSavedListMessage();
    }
}
