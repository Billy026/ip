package bob.managers;

import bob.exceptions.InvalidCommandException;

/**
 * Converts types from one to another.
 */
public abstract class ConversionManager {
    /**
     * Converts a string input to an index.
     * 
     * @param str string to convert.
     * @param errorMessage error message if string is not an integer.
     * @return integer value of string.
     * @throws InvalidCommandException if string is not an integer.
     */
    public static int convertInputToIndex(String str, String errorMessage) throws InvalidCommandException {
        try {
            int index = Integer.parseInt(str);
            return index;
        } catch (NumberFormatException e) {
            throw new InvalidCommandException(errorMessage);
        }
    }
}
