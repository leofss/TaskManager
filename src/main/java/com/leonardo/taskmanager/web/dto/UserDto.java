package com.leonardo.taskmanager.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    private String username;

    @NotBlank
    @Email(message = "Invalid e-mail address",  regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Pattern(regexp = "ADMIN|USER", message = "Role must be one of: ADMIN, BROKER, CAPTATION")
    private String role;


}
