package com.innowise.authenticationservice.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    boolean adminRole(Authentication authentication);
}
