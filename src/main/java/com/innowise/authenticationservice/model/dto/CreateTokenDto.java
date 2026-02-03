package com.innowise.authenticationservice.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenDto {
    @NotBlank(message = "Token can't be empty")
    private String token;
    private LocalDate expirationDate;
    private Long userId;
}
