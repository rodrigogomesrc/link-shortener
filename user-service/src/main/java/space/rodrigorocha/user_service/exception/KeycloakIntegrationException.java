package space.rodrigorocha.user_service.exception;

public class KeycloakIntegrationException extends RuntimeException {
    public KeycloakIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}