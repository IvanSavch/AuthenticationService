package com.innowise.authenticationservice.exception;

public class InvalidTokenException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Invalid token";
    public InvalidTokenException() {
        super(DEFAULT_MESSAGE);
    }
}
