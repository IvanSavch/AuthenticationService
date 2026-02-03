package com.innowise.authenticationservice.service;

import com.innowise.authenticationservice.model.dto.UserDto;
import com.innowise.authenticationservice.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    User save(UserDto userDto);
    User findByLogin(String login);
    User findById(Long id);
    PasswordEncoder passwordEncoder();
}
