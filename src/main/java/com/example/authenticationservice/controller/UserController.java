package com.example.authenticationservice.controller;

import com.example.authenticationservice.auth.JWTTokenProvider;
import com.example.authenticationservice.model.dto.UserCreateDto;
import com.example.authenticationservice.model.entity.User;
import com.example.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody UserCreateDto userCreateDto){
        User save = userService.save(userCreateDto);
        return ResponseEntity.ok(save);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserCreateDto userCreateDto){
        User byLogin = userService.findByLogin(userCreateDto.getLogin());
        String token = jwtTokenProvider.generateToken(byLogin.getId(), byLogin.getLogin(), byLogin.getPassword(), byLogin.getRole());
        return ResponseEntity.ok(token);
    }
}
