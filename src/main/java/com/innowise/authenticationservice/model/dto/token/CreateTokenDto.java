package com.innowise.authenticationservice.model.dto.token;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenDto {
    @NotBlank(message = "Token can't be empty")
    private String token;
    private LocalDateTime expirationDate;
    private Long userId;
}
