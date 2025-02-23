package bob.managers;

import java.nio.file.Paths;

import bob.exceptions.InvalidCommandException;
import bob.parser.Parser;

/**
 * Manages all functions related to the UI.
 */
public class UiManager {
    // File path to save in hard disk
    private static final String FILE_PATH = Paths.get("data", "bob.txt").toString();
    private final Parser parser = new Parser(FILE_PATH);

    /**
     * Propogates displayIncomingDeadlines to parser.
     *
     * @return string returned by displayIncomingDeadlines.
     */
    public String getIncomingDeadlines() {
        return this.parser.displayIncomingDeadlines();
    }

    /**
     * Executes the user's command.
     *
     * @param input user input.
     * @return string with the output of the user input.
     */
    public String executeUserCommand(String input) {
        String[] inputArray = input.split(" ");

        try {
            return this.parser.parseCommand(inputArray);
        } catch (InvalidCommandException e) {
            return e.getMessage() + "\n";
        }
    }

    /**
     * Propogates getSavedListMessage to parser.
     *
     * @return output returned by getSavedListMessage.
     */
    public String getSavedListMessage() {
        return this.parser.getSavedListMessage();
    }
}
