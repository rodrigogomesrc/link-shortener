package space.rodrigorocha.short_url.short_url_creator.exception;

public class MaxRetriesReachedException extends RuntimeException {
    public MaxRetriesReachedException(String message) {
        super(message);
    }
}
