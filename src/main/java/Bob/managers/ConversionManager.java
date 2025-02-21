package bob.managers;

import bob.exceptions.InvalidCommandException;
import bob.exceptions.InvalidDateFormatException;

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

    /**
     * Converts the array of date strings into integers.
     *
     * @param strArray array of strings.
     * @return array of integers.
     * @throws InvalidDateFormatException string cannot be converted into integer.
     */
    public static int[] convertToNumerics(String[] strArray, String message) throws InvalidDateFormatException {
        int[] numericParts = new int[strArray.length];
        try {
            for (int i = 0; i < strArray.length; i++) {
                numericParts[i] = Integer.parseInt(strArray[i]);
            }
        } catch (NumberFormatException e) {
            throw new InvalidDateFormatException(message);
        }

        return numericParts;
    }
}
