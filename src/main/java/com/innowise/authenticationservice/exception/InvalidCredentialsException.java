package com.innowise.authenticationservice.exception;

import java.io.Serial;

public class InvalidCredentialsException extends RuntimeException{
    private static final String DEFAULT_MESSAGE="Invalid login or password";
    @Serial
    private static final long serialVersionUID = -541436134216647999L;

    public InvalidCredentialsException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }

    protected InvalidCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
