package io.github.cristhianm30.spacex_launches_back.domain.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message) {
        super(message);
    }
    
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
