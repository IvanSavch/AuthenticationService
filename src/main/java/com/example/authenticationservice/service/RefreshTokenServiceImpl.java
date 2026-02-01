package com.example.authenticationservice.service;

import com.example.authenticationservice.auth.JWTAccessTokenProvider;
import com.example.authenticationservice.auth.JWTRefreshTokenProvider;
import com.example.authenticationservice.model.entity.RefreshToken;
import com.example.authenticationservice.model.entity.User;
import com.example.authenticationservice.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JWTRefreshTokenProvider jwtRefreshTokenProvider;
    private final JWTAccessTokenProvider jwtAccessTokenProvider;

    public RefreshTokenServiceImpl(RefreshTokenRepository repository, UserService userService, JWTRefreshTokenProvider jwtRefreshTokenProvider, JWTAccessTokenProvider jwtAccessTokenProvider) {
        this.refreshTokenRepository = repository;
        this.userService = userService;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
    }

    @Override
    @Transactional
    public RefreshToken save(Long userId, String token, LocalDate expirationDate) {
        User byId = userService.findById(userId);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpirationDate(expirationDate);
        refreshToken.setUser(byId);
        refreshToken.setToken(token);
        if (refreshTokenRepository.findByUser_Id(userId) != null) {
            refreshTokenRepository.deleteByUserId(userId);
        }
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public String refresh(String token) {
        Long userIdFromRefresh = jwtRefreshTokenProvider.getUserIdFromRefresh(token);
        User byId = userService.findById(userIdFromRefresh);
        return jwtAccessTokenProvider.generateToken(byId.getId(), byId.getLogin(), byId.getRole());
    }
}
