package io.github.cristhianm30.spacex_launches_back.domain.exception;

public class LaunchNotFoundException extends RuntimeException {
    public LaunchNotFoundException(String message) {
        super(message);
    }
}