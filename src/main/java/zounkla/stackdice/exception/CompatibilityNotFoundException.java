package zounkla.stackdice.exception;

public class CompatibilityNotFoundException extends Exception {
    public CompatibilityNotFoundException(String message) {
        super(message);
    }

    public CompatibilityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
