package com.example.authenticationservice.service;

import com.example.authenticationservice.mapper.UserMapper;
import com.example.authenticationservice.model.dto.UserCreateDto;
import com.example.authenticationservice.model.entity.User;
import com.example.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(UserCreateDto userCreateDto) {
        User user = userMapper.toUser(userCreateDto);
        user.setRole(User.Role.ROLE_USER);
        return userRepository.save(user);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
