package com.innowise.authenticationservice.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE="Login already exist";

    public LoginAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public LoginAlreadyExistsException(String message) {
        super(message);
    }

    public LoginAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected LoginAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
