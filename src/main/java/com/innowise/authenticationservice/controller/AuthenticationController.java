package com.innowise.authenticationservice.controller;

import com.innowise.authenticationservice.jwt.JWTAccessTokenProvider;
import com.innowise.authenticationservice.jwt.JWTRefreshTokenProvider;
import com.innowise.authenticationservice.exception.InvalidCredentialsException;
import com.innowise.authenticationservice.model.dto.CreateTokenDto;
import com.innowise.authenticationservice.model.dto.RefreshTokenDto;
import com.innowise.authenticationservice.model.dto.TokenResponse;
import com.innowise.authenticationservice.model.dto.UserDto;
import com.innowise.authenticationservice.model.dto.ValidationTokenRequest;
import com.innowise.authenticationservice.model.dto.ValidationTokenResponse;
import com.innowise.authenticationservice.model.entity.User;
import com.innowise.authenticationservice.service.RefreshTokenService;
import com.innowise.authenticationservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class AuthenticationController {
    private final UserService userService;
    private final JWTAccessTokenProvider jwtAccessTokenProvider;
    private final JWTRefreshTokenProvider jwtRefreshTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(UserService userService, JWTAccessTokenProvider jwtAccessTokenProvider, JWTRefreshTokenProvider jwtRefreshTokenProvider, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping
    public ResponseEntity<User> registration(@Valid @RequestBody UserDto userDto) {
        User save = userService.save(userDto);
        return ResponseEntity.ok(save);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserDto userDto) {
        User byLogin = userService.findByLogin(userDto.getLogin());
        if (!userService.passwordEncoder().matches(userDto.getPassword(), byLogin.getPassword())) {
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
    public ResponseEntity<ValidationTokenResponse> validate(ValidationTokenRequest validationTokenRequest) {
        return ResponseEntity.ok(refreshTokenService.validationToken(validationTokenRequest));
    }
}
