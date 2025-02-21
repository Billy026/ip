package bob.exceptions;

/**
 * Exception for when the user enters an invalid command
 */
public class InvalidCommandException extends Exception {
    /**
     * Primary constructor of InvalidCOmmandException.
     *
     * @param message specialised message.
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
