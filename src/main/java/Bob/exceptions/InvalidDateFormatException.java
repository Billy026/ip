package Bob.exceptions;

/**
 * Exception for when dates are formatted incorrectly.
 */
public class InvalidDateFormatException extends InvalidCommandException {
    /**
     * Primary constructor
     * 
     * @param message Specialised message
     */
    public InvalidDateFormatException(String message) {
        super(message);
    }
}
