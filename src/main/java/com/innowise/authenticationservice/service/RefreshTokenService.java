package com.innowise.authenticationservice.service;

import com.innowise.authenticationservice.model.dto.CreateTokenDto;
import com.innowise.authenticationservice.model.dto.TokenResponse;

import com.innowise.authenticationservice.model.entity.RefreshToken;

import java.time.LocalDateTime;

public interface RefreshTokenService {
     RefreshToken create (CreateTokenDto createTokenDto);
     TokenResponse refresh(String token);
     void deleteExpiredTokens();
}
