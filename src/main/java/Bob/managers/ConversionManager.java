package Bob.managers;

import Bob.exceptions.InvalidCommandException;

public abstract class ConversionManager {
    public static int convertInputToIndex(String str, String errorMessage) throws InvalidCommandException {
        try {
            int index = Integer.parseInt(str);
            return index;
        } catch (NumberFormatException e) {
            throw new InvalidCommandException(errorMessage);
        }
    }
}
