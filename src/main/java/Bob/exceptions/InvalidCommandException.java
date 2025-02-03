package Bob.exceptions;

/**
 * Exception for when the user enters an invalid command
 */
public class InvalidCommandException extends Exception {
    /**
     * Primary constructor
     * 
     * @param message Specialised message
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
