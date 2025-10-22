package astra.exception;

public class GpaInputException extends RuntimeException {
    public GpaInputException(String message) {
        super(message);
    }

    public GpaInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
