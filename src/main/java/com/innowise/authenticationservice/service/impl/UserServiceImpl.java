package com.innowise.authenticationservice.service.impl;

import com.innowise.authenticationservice.exception.InvalidCredentialsException;
import com.innowise.authenticationservice.exception.LoginAlreadyExistsException;
import com.innowise.authenticationservice.exception.UserNotFoundException;
import com.innowise.authenticationservice.mapper.UserMapper;
import com.innowise.authenticationservice.model.dto.UserCreateDto;
import com.innowise.authenticationservice.model.entity.User;
import com.innowise.authenticationservice.repository.UserRepository;
import com.innowise.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

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
        if (userRepository.findByLogin(userCreateDto.getLogin()).isPresent()){
            throw new LoginAlreadyExistsException();
        }
        User user = userMapper.toUser(userCreateDto);
        user.setRole(User.Role.ROLE_USER);
        String passwordEncodeWithSalt = addSalt(passwordEncoder().encode(user.getPassword()));
        user.setPassword(passwordEncodeWithSalt);
        return userRepository.save(user);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(InvalidCredentialsException::new);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
    @Override
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private String addSalt(String coderCred) {
        String symbols = "qwertyuiopasdfghjklzxcvbnm1234567890";
        String salt = new Random().ints(10, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
        return salt + coderCred;
    }

}
