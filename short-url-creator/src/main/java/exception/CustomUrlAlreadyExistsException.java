package exception;

public class CustomUrlAlreadyExistsException extends RuntimeException {
    public CustomUrlAlreadyExistsException(String message) {
        super(message);
    }
}
