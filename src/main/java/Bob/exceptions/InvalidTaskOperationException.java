package Bob.exceptions;

/**
 * Exception for when there is an invalid operation in one of the Task functions
 */
public class InvalidTaskOperationException extends InvalidCommandException {
    /**
     * Primary constructor of InvalidTaskOperationException.
     * 
     * @param message specialised message.
     */
    public InvalidTaskOperationException(String message) {
        super(message);
    }
}
