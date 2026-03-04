package com.innowise.authenticationservice.exception;

import java.io.Serial;

public class InvalidTokenException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Invalid token";
    @Serial
    private static final long serialVersionUID = 5268459961654758908L;

    public InvalidTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenException(Throwable cause) {
        super(cause);
    }

    protected InvalidTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
