package com.innowise.authenticationservice.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User not found.";
    @Serial
    private static final long serialVersionUID = -6362189458211839810L;

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    protected UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
