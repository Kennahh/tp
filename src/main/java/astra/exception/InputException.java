package astra.exception;

/**
 * Signals that user input is malformed or invalid.
 */
public class InputException extends Exception {

    /**
     * Creates an InputException.
     *
     * @param message Error message.
     */
    public InputException(String message) {
        super(message);
    }
}
