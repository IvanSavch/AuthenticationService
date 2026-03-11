package com.innowise.authenticationservice.service;

import com.innowise.authenticationservice.model.dto.token.CreateTokenDto;
import com.innowise.authenticationservice.model.dto.token.TokenResponse;

import com.innowise.authenticationservice.model.entity.RefreshToken;

public interface RefreshTokenService {
     RefreshToken create (CreateTokenDto createTokenDto);
     TokenResponse refresh(String token);
     void deleteExpiredTokens();
}
