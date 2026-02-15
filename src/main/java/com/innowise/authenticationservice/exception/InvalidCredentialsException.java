package com.innowise.authenticationservice.exception;

public class InvalidCredentialsException extends RuntimeException{
    private static final String DEFAULT_MESSAGE="Invalid login or password";
    public InvalidCredentialsException() {
        super(DEFAULT_MESSAGE);
    }
}
