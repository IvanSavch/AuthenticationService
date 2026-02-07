package com.innowise.authenticationservice.repository;

import com.innowise.authenticationservice.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    @Query(nativeQuery = true,value = "delete from refresh_token where user_id = ?1")
    @Modifying
    void deleteByUserId(Long userId);
    @Query(nativeQuery = true,value = "select * from refresh_token where user_id = ?1")
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByToken(String token);
    @Query(nativeQuery = true,value = "delete  from refresh_token where expiration_date < ?1")
    void deleteByExpiryDateBefore(LocalDateTime now);
}
