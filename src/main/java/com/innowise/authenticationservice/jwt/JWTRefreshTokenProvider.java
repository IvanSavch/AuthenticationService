package com.innowise.authenticationservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JWTRefreshTokenProvider {

    @Value("${jwt.refresh.secret}")
    private String jwtSecret;
    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshExpirationInMs;

    private SecretKey refreshKey;
    @PostConstruct
    public void init() {
        this.refreshKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(Long id) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtRefreshExpirationInMs);
        return  Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(now)
                .expiration(validity)
                .claim("id", id)
                .signWith(refreshKey, Jwts.SIG.HS256)
                .compact();
    }
    public LocalDateTime getDateExpirationFromRefresh(String token){
        Date expiration = Jwts.parser().
                verifyWith(refreshKey).
                build().
                parseSignedClaims(token).
                getPayload().
                getExpiration();
        return LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());

    }
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(refreshKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
