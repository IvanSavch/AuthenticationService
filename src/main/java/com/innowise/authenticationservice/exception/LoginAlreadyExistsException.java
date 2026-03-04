package com.innowise.authenticationservice.exception;

import java.io.Serial;

public class LoginAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE="Login already exist";
    @Serial
    private static final long serialVersionUID = 4054866389350828503L;

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
