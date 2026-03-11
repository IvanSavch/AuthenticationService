package com.innowise.authenticationservice.controller;

import com.innowise.authenticationservice.exception.InvalidTokenException;
import com.innowise.authenticationservice.jwt.JWTAccessTokenProvider;
import com.innowise.authenticationservice.jwt.JWTRefreshTokenProvider;
import com.innowise.authenticationservice.exception.InvalidCredentialsException;
import com.innowise.authenticationservice.mapper.UserMapper;
import com.innowise.authenticationservice.model.dto.token.CreateTokenDto;
import com.innowise.authenticationservice.model.dto.token.RefreshTokenDto;
import com.innowise.authenticationservice.model.dto.token.TokenResponse;
import com.innowise.authenticationservice.model.dto.user.UserCreateDto;
import com.innowise.authenticationservice.model.dto.user.UserLoginDto;
import com.innowise.authenticationservice.model.dto.user.UserResponse;
import com.innowise.authenticationservice.model.dto.token.ValidationAccessTokenRequest;
import com.innowise.authenticationservice.model.entity.User;
import com.innowise.authenticationservice.service.RefreshTokenService;
import com.innowise.authenticationservice.service.UserService;
import com.innowise.authenticationservice.util.PasswordUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final PasswordUtil passwordUtil;


    public AuthenticationController(UserService userService, JWTAccessTokenProvider jwtAccessTokenProvider, JWTRefreshTokenProvider jwtRefreshTokenProvider, RefreshTokenService refreshTokenService, UserMapper userMapper, PasswordUtil passwordUtil) {
        this.userService = userService;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
        this.passwordUtil = passwordUtil;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserResponse> registration(@Valid @RequestBody UserCreateDto userCreateDto) {
        User save = userService.save(userCreateDto);
        UserResponse userResponse = userMapper.toUserResponse(save);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        User byLogin = userService.findByLogin(userLoginDto.getLogin());
        if (!passwordUtil.matches(userLoginDto.getPassword(), byLogin.getPassword())) {
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
    @PatchMapping("/{id}")
    @PreAuthorize("@authenticationServiceImpl.adminRole(authentication)")
    public ResponseEntity<UserResponse> updateRoleById(@PathVariable Long id){
        User user = userService.updateRoleById(id);
        UserResponse userResponse = userMapper.toUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }
}
