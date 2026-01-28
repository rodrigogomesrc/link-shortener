package exception;

public class MaxRetriesReachedException extends Exception{
    public MaxRetriesReachedException(String message) {
        super(message);
    }
}
