package Bob.exceptions;

/**
 * Exception for when there is an invalid operation in one of the Task functions
 */
public class InvalidTaskOperationException extends InvalidCommandException {
    /**
     * Primary constructor
     * 
     * @param message Specialised message
     */
    public InvalidTaskOperationException(String message) {
        super(message);
    }
}
