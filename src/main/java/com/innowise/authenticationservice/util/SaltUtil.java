package com.innowise.authenticationservice.util;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;

@Component
public class SaltUtil {
    public String addSalt(String coderCred) {
        String symbols = "qwertyuiopasdfghjklzxcvbnm1234567890";
        String salt = new Random().ints(10, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
        return salt + coderCred;
    }
}
