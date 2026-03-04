package com.innowise.authenticationservice.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserServiceDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
}
