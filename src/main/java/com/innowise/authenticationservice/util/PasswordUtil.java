package com.innowise.authenticationservice.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    private final PasswordEncoder passwordEncoder;
    private final SaltUtil saltUtil;

    public PasswordUtil(PasswordEncoder passwordEncoder, SaltUtil saltUtil) {
        this.passwordEncoder = passwordEncoder;
        this.saltUtil = saltUtil;
    }

    public boolean matches(String rawPassword, String passwordWithSalt) {
        String passwordWithoutSalt = removeSalt(passwordWithSalt);
        return passwordEncoder.matches(rawPassword, passwordWithoutSalt);
    }

    public String encode(String password) {
        String encode = passwordEncoder.encode(password);
        return saltUtil.addSalt(encode);
    }

    private String removeSalt(String passwordWithSalt) {
        return passwordWithSalt.substring(10);
    }

}
