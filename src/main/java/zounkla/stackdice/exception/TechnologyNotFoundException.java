package zounkla.stackdice.exception;

public class TechnologyNotFoundException extends Exception {
    public TechnologyNotFoundException(String message) {
        super(message);
    }

    public TechnologyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
