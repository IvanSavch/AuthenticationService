package com.innowise.authenticationservice.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginDto {
    @NotBlank(message = "Login can't be empty")
    private String login;
    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, message = "Password size must be 8 or more")
    private String password;
}
