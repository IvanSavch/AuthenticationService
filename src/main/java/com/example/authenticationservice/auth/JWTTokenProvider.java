package com.example.authenticationservice.auth;

import com.example.authenticationservice.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class JWTTokenProvider {

    private String jwtSecret= "this-is-a-very-long-secret-key-at-least-32-bytes!!";


    private long jwtExpirationInMs = 36000;

    private final SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    public String generateToken(Long id, String login,String password, User.Role role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationInMs);
        return  Jwts.builder()
                .subject(login)
                .issuedAt(now)
                .expiration(validity)
                .claim("id", id)
                .claim("password", password)
                .claim("role", role)
                .signWith(key, Jwts.SIG.HS256)
                .compact();

    }

    public Authentication getAuthentication(String token) {
        User userDetails = new User();
        userDetails.setId(getUserIdFromJWT(token));
        userDetails.setPassword(getUserPasswordFromJWT(token));
        userDetails.setRole(getUserRolesFromJWT(token));
        return new UsernamePasswordAuthenticationToken(userDetails, token);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserLoginFromJWT(String token) {
        return Jwts.parser().
                verifyWith(key).
                build().
                parseSignedClaims(token).
                getPayload().
                get("login", String.class);
    }

    public Long getUserIdFromJWT(String token) {
        return Jwts.parser().
                verifyWith(key).
                build().
                parseSignedClaims(token).
                getPayload().
                get("id", Long.class);
    }

    public String getUserPasswordFromJWT(String token) {
        return  Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("password", String.class);
    }

    public User.Role getUserRolesFromJWT(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles",User.Role.class);
    }

//    private String getUserRoleNamesFromJWT(User.Role role) {
//        return role.name();
//    }
//
//    private User.Role getUserRoleNamesFromJWT(List<String> roles) {
//        Set<User.Role> result = new HashSet<>();
//        roles.forEach(s -> result.add(User.Role.valueOf(s)));
//        return result;
//    }
}
