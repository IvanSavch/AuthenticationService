package com.example.authenticationservice.service;

import com.example.authenticationservice.model.dto.UserCreateDto;
import com.example.authenticationservice.model.entity.User;

import java.util.List;

public interface UserService {
    User save(UserCreateDto userCreateDto);
    User findByLogin(String login);
    User findById(Long id);
}
