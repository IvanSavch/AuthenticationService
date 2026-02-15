package com.innowise.authenticationservice.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE="Login already exist";

    public LoginAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }
}
