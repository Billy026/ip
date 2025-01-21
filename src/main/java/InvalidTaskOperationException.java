/**
 * Exception for when there is an invalid operation in one of the Task functions
 */
public class InvalidTaskOperationException extends Exception {
    /**
     * Default constructor
     */
    public InvalidTaskOperationException() {}

    /**
     * Primary constructor
     * 
     * @param message Specialised message
     */
    public InvalidTaskOperationException(String message) {
        super(message);
    }
}