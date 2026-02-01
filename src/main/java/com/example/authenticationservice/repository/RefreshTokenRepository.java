package com.example.authenticationservice.repository;

import com.example.authenticationservice.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    @Query(nativeQuery = true,value = "delete from refresh_token rf where user_id = ?1")
    @Modifying
    void deleteByUserId(Long userId);
    RefreshToken findByUser_Id(Long userId);
}
