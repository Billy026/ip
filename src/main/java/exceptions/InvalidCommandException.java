package exceptions;

/**
 * Exception for when the user enters an invalid command
 */
public class InvalidCommandException extends Exception {
    /**
     * Default constructor
     */
    public InvalidCommandException() {}

    /**
     * Primary constructor
     * 
     * @param message Specialised message
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}