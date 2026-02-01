package com.example.authenticationservice.controller;

import com.example.authenticationservice.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TokenController {

    private final RefreshTokenService refreshTokenService;
    public TokenController(RefreshTokenService refreshTokenService) {

        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(String refreshToken) {
        return ResponseEntity.ok(refreshTokenService.refresh(refreshToken));
    }
}
