package com.innowise.authenticationservice.controller;

import com.innowise.authenticationservice.exception.InvalidTokenException;
import com.innowise.authenticationservice.jwt.JWTAccessTokenProvider;
import com.innowise.authenticationservice.jwt.JWTRefreshTokenProvider;
import com.innowise.authenticationservice.exception.InvalidCredentialsException;
import com.innowise.authenticationservice.mapper.UserMapper;
import com.innowise.authenticationservice.model.dto.CreateTokenDto;
import com.innowise.authenticationservice.model.dto.RefreshTokenDto;
import com.innowise.authenticationservice.model.dto.TokenResponse;
import com.innowise.authenticationservice.model.dto.UserCreateDto;
import com.innowise.authenticationservice.model.dto.UserResponse;
import com.innowise.authenticationservice.model.dto.ValidationAccessTokenRequest;
import com.innowise.authenticationservice.model.entity.User;
import com.innowise.authenticationservice.service.RefreshTokenService;
import com.innowise.authenticationservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final JWTAccessTokenProvider jwtAccessTokenProvider;
    private final JWTRefreshTokenProvider jwtRefreshTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationController(UserService userService, JWTAccessTokenProvider jwtAccessTokenProvider, JWTRefreshTokenProvider jwtRefreshTokenProvider, RefreshTokenService refreshTokenService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/")
    public ResponseEntity<UserResponse> registration(@Valid @RequestBody UserCreateDto userCreateDto) {
        User save = userService.save(userCreateDto);
        UserResponse userResponse = userMapper.toUserResponse(save);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserCreateDto userCreateDto) {
        User byLogin = userService.findByLogin(userCreateDto.getLogin());
        if (!passwordEncoder.matches(userCreateDto.getPassword(), byLogin.getPassword().substring(10))) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtAccessTokenProvider.generateToken(byLogin.getId(), byLogin.getLogin(), byLogin.getRole());
        String refreshToken = jwtRefreshTokenProvider.generateToken(byLogin.getId());

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);

        refreshTokenService.create(new CreateTokenDto(refreshToken, jwtRefreshTokenProvider.getDateExpirationFromRefresh(refreshToken), byLogin.getId()));

        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(refreshTokenService.refresh(refreshTokenDto.getRefreshToken()));
    }

    @PostMapping("/validate")
    public ResponseEntity<UserResponse> validate(@RequestBody ValidationAccessTokenRequest validationAccessTokenRequest) {
        if (!jwtAccessTokenProvider.validateToken(validationAccessTokenRequest.getToken())) {
            throw new InvalidTokenException();
        }
        Long userIdFromJWT = jwtAccessTokenProvider.getUserIdFromJWT(validationAccessTokenRequest.getToken());
        User byId = userService.findById(userIdFromJWT);
        return ResponseEntity.ok().body(userMapper.toUserResponse(byId));
    }
}
