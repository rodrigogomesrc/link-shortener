package space.rodrigorocha.short_url.short_url_creator.exception;

public class CustomUrlAlreadyExistsException extends RuntimeException {
    public CustomUrlAlreadyExistsException(String message) {
        super(message);
    }
}
