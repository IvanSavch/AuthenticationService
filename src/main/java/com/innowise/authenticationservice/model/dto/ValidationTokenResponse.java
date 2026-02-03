package com.innowise.authenticationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationTokenResponse {
    private LocalDate expirationDate;
    private Long userId;
}

