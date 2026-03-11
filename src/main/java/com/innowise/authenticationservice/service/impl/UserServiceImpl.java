package com.innowise.authenticationservice.service.impl;

import com.innowise.authenticationservice.client.UserClient;
import com.innowise.authenticationservice.exception.InvalidCredentialsException;
import com.innowise.authenticationservice.exception.LoginAlreadyExistsException;
import com.innowise.authenticationservice.exception.ServiceUnavailableException;
import com.innowise.authenticationservice.exception.UserNotFoundException;
import com.innowise.authenticationservice.mapper.UserMapper;
import com.innowise.authenticationservice.model.dto.user.CreateUserServiceDto;
import com.innowise.authenticationservice.model.dto.user.UserCreateDto;
import com.innowise.authenticationservice.model.entity.User;
import com.innowise.authenticationservice.repository.UserRepository;
import com.innowise.authenticationservice.service.UserService;
import com.innowise.authenticationservice.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserClient userClient;
    private final PasswordUtil passwordUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserClient userClient, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userClient = userClient;
        this.passwordUtil = passwordUtil;
    }

    @Override
    @Transactional
    public User save(UserCreateDto userCreateDto) {
        if (userRepository.findByLogin(userCreateDto.getLogin()).isPresent()) {
            throw new LoginAlreadyExistsException();
        }

        User save = null;
        try {
            User user = userMapper.toUser(userCreateDto);
            user.setRole(User.Role.ROLE_USER);
            String passwordEncodeWithSalt = passwordUtil.encode(user.getPassword());
            user.setPassword(passwordEncodeWithSalt);
            save = userRepository.save(user);

            CreateUserServiceDto createUserServiceDto = userMapper.toCreateUserServiceDto(userCreateDto);
            createUserServiceDto.setId(save.getId());

            userClient.create(createUserServiceDto);

            return save;

        } catch (InvalidCredentialsException e) {
            if (save != null) {
                userRepository.delete(save);
            }
            throw new InvalidCredentialsException("Email already exist");
        } catch (ServiceUnavailableException e) {
            if (save != null) {
                userRepository.delete(save);
            }
            throw new ServiceUnavailableException("User service unavailable during user creation");
        }
    }

    @Override
    public User updateRoleById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setRole(User.Role.ROLE_ADMIN);
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
}
