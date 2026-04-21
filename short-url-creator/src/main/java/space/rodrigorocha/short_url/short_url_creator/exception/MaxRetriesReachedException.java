package space.rodrigorocha.short_url.short_url_creator.exception;

public class MaxRetriesReachedException extends Exception {
    public MaxRetriesReachedException(String message) {
        super(message);
    }
}
