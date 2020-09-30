package lt.verbus.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
