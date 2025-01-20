/**
 * Exception for when there is an invalid operation in one of the Task functions
 */
public class InvalidTaskOperationException extends Exception {
    public InvalidTaskOperationException() {}

    public InvalidTaskOperationException(String message) {
        super(message);
    }
}