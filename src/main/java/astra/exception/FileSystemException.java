package astra.exception;

/**
 * Indicates failure in file system operations, such as to tasks.txt file.
 */
public class FileSystemException extends Exception {

    /**
     * Creates a FileSystemException.
     *
     * @param message Error message.
     */
    public FileSystemException(String message) {
        super(message);
    }
}

