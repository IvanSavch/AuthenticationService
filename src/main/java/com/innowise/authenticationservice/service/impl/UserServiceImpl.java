package com.innowise.authenticationservice.service.impl;

import com.innowise.authenticationservice.client.UserClient;
import com.innowise.authenticationservice.exception.InvalidCredentialsException;
import com.innowise.authenticationservice.exception.LoginAlreadyExistsException;
import com.innowise.authenticationservice.exception.UserNotFoundException;
import com.innowise.authenticationservice.mapper.UserMapper;
import com.innowise.authenticationservice.model.dto.user.CreateUserServiceDto;
import com.innowise.authenticationservice.model.dto.user.UserCreateDto;
import com.innowise.authenticationservice.model.entity.User;
import com.innowise.authenticationservice.repository.UserRepository;
import com.innowise.authenticationservice.service.UserService;
import com.innowise.authenticationservice.util.SaltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;
    private final SaltUtil saltUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, UserClient userClient, SaltUtil saltUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userClient = userClient;
        this.saltUtil = saltUtil;
    }

    @Override
    public User save(UserCreateDto userCreateDto) {
        if (userRepository.findByLogin(userCreateDto.getLogin()).isPresent()) {
            throw new LoginAlreadyExistsException();
        }
        User save = null;
        try {
            User user = userMapper.toUser(userCreateDto);
            user.setRole(User.Role.ROLE_USER);
            String passwordEncodeWithSalt = saltUtil.addSalt(passwordEncoder.encode(user.getPassword()));
            user.setPassword(passwordEncodeWithSalt);
            save = userRepository.save(user);

            CreateUserServiceDto createUserServiceDto = userMapper.toCreateUserServiceDto(userCreateDto);
            createUserServiceDto.setAuthId(save.getId());
            userClient.create(createUserServiceDto);
            return save;
        } catch (Exception e) {
            if (save != null) {
                userRepository.delete(save);
            }
            throw new InvalidCredentialsException();
        }
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(InvalidCredentialsException::new);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

}
