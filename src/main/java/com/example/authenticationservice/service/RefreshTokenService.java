package com.example.authenticationservice.service;

import com.example.authenticationservice.model.entity.RefreshToken;

import java.time.LocalDate;

public interface RefreshTokenService {
     RefreshToken save (Long userId, String refreshToken, LocalDate expirationDate);
     String refresh(String token);
}
