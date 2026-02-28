package com.innowise.authenticationservice.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Login can't be empty")
    private String login;
    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, message = "Password size must be 8 or more")
    private String password;
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Surname can't be empty")
    private String surname;
    @NotNull(message = "Birth date can't be null")
    private LocalDate birthDate;
    @NotBlank(message = "Email date can't be empty")
    @Email(message = "Email not valid")
    private String email;
}
