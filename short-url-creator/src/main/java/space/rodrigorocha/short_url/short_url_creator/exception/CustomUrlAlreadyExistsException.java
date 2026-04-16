package space.rodrigorocha.short_url.short_url_creator.exception;

public class CustomUrlAlreadyExistsException extends Exception {
    public CustomUrlAlreadyExistsException(String message) {
        super(message);
    }
}
