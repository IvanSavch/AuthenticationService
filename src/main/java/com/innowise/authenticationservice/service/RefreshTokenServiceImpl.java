package com.innowise.authenticationservice.service;

import com.innowise.authenticationservice.jwt.JWTAccessTokenProvider;
import com.innowise.authenticationservice.jwt.JWTRefreshTokenProvider;
import com.innowise.authenticationservice.exception.InvalidTokenException;
import com.innowise.authenticationservice.mapper.TokenMapper;
import com.innowise.authenticationservice.model.dto.CreateTokenDto;
import com.innowise.authenticationservice.model.dto.TokenResponse;
import com.innowise.authenticationservice.model.dto.ValidationTokenRequest;
import com.innowise.authenticationservice.model.dto.ValidationTokenResponse;
import com.innowise.authenticationservice.model.entity.RefreshToken;
import com.innowise.authenticationservice.model.entity.User;
import com.innowise.authenticationservice.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JWTRefreshTokenProvider jwtRefreshTokenProvider;
    private final JWTAccessTokenProvider jwtAccessTokenProvider;
    private final TokenMapper tokenMapper;

    public RefreshTokenServiceImpl(RefreshTokenRepository repository, UserService userService, JWTRefreshTokenProvider jwtRefreshTokenProvider, JWTAccessTokenProvider jwtAccessTokenProvider, TokenMapper tokenMapper) {
        this.refreshTokenRepository = repository;
        this.userService = userService;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.tokenMapper = tokenMapper;
    }

    @Override
    @Transactional
    public RefreshToken create(CreateTokenDto createTokenDto) {
        User byId = userService.findById(createTokenDto.getUserId());
        RefreshToken refreshToken = tokenMapper.toToken(createTokenDto);
        refreshToken.setUser(byId);

        if (refreshTokenRepository.findByUserId(byId.getId()).isPresent()) {
            refreshTokenRepository.deleteByUserId(byId.getId());
        }

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public TokenResponse refresh(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(InvalidTokenException::new);

        if (!jwtRefreshTokenProvider.validateToken(refreshToken.getToken())) {
            throw new InvalidTokenException();
        }

        User byId = userService.findById(refreshToken.getUser().getId());

        String newAccess = jwtAccessTokenProvider.generateToken(byId.getId(), byId.getLogin(), byId.getRole());
        String newRefreshToken = jwtRefreshTokenProvider.generateToken(byId.getId());

        refreshToken.setToken(newRefreshToken);
        refreshToken.setExpirationDate(jwtRefreshTokenProvider.getDateExpirationFromRefresh(newRefreshToken));

        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(newAccess, newRefreshToken);
    }

    @Override
    public ValidationTokenResponse validationToken(ValidationTokenRequest validationTokenRequest) {
        if (!jwtRefreshTokenProvider.validateToken(validationTokenRequest.getToken())) {
            throw new InvalidTokenException();
        }
        LocalDate dateExpirationFromRefresh = jwtRefreshTokenProvider.getDateExpirationFromRefresh(validationTokenRequest.getToken());
        Long userIdFromRefresh = jwtRefreshTokenProvider.getUserIdFromRefresh(validationTokenRequest.getToken());

        return new ValidationTokenResponse(dateExpirationFromRefresh, userIdFromRefresh);
    }
}
