package recipes;

public class NoObjectInDatabaseException extends RuntimeException {
    public NoObjectInDatabaseException(String message) {
        super(message);
    }
}