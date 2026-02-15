package com.innowise.authenticationservice.service;

import com.innowise.authenticationservice.model.dto.UserCreateDto;
import com.innowise.authenticationservice.model.entity.User;

public interface UserService {
    User save(UserCreateDto userCreateDto);
    User findByLogin(String login);
    User findById(Long id);
}
