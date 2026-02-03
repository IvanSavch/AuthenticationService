package com.innowise.authenticationservice.service;

import com.innowise.authenticationservice.model.dto.CreateTokenDto;
import com.innowise.authenticationservice.model.dto.TokenResponse;
import com.innowise.authenticationservice.model.dto.ValidationTokenRequest;
import com.innowise.authenticationservice.model.dto.ValidationTokenResponse;
import com.innowise.authenticationservice.model.entity.RefreshToken;

public interface RefreshTokenService {
     RefreshToken create (CreateTokenDto createTokenDto);
     TokenResponse refresh(String token);
     ValidationTokenResponse validationToken(ValidationTokenRequest validationTokenRequest);
}
