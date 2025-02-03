package Bob.exceptions;

/**
 * Exception for when dates are formatted incorrectly.
 */
public class InvalidDateFormatException extends InvalidCommandException {
    /**
     * Primary constructor of InvalidDateFormatException.
     * 
     * @param message specialised message.
     */
    public InvalidDateFormatException(String message) {
        super(message);
    }
}
