package com.example.authenticationservice.service;

import com.example.authenticationservice.model.dto.UserCreateDto;
import com.example.authenticationservice.model.entity.User;

public interface UserService {
    User save(UserCreateDto userCreateDto);
    User findByLogin(String login);

}
