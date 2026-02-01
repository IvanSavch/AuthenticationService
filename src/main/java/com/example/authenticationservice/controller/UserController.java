package com.example.authenticationservice.controller;

import com.example.authenticationservice.auth.JWTAccessTokenProvider;
import com.example.authenticationservice.auth.JWTRefreshTokenProvider;
import com.example.authenticationservice.model.dto.TokenResponse;
import com.example.authenticationservice.model.dto.UserCreateDto;
import com.example.authenticationservice.model.entity.User;
import com.example.authenticationservice.service.RefreshTokenService;
import com.example.authenticationservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final JWTAccessTokenProvider jwtAccessTokenProvider;
    private final JWTRefreshTokenProvider jwtRefreshTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, JWTAccessTokenProvider jwtAccessTokenProvider, JWTRefreshTokenProvider jwtRefreshTokenProvider, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody UserCreateDto userCreateDto){
        User save = userService.save(userCreateDto);
        return ResponseEntity.ok(save);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserCreateDto userCreateDto){
        User byLogin = userService.findByLogin(userCreateDto.getLogin());
        String accessToken = jwtAccessTokenProvider.generateToken(byLogin.getId(), byLogin.getLogin(), byLogin.getRole());
        String refreshToken = jwtRefreshTokenProvider.generateToken(byLogin.getId());

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);

        refreshTokenService.save(byLogin.getId(),refreshToken,jwtRefreshTokenProvider.getDateExpirationFromRefresh(refreshToken));

        return ResponseEntity.ok().body(tokenResponse);
    }

}
