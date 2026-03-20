package com.example.kokoni.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
    @NotBlank(message = "El nombre de usuario es obligatorio")
    String username,
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email no válido")
    String email,
    String avatarUrl,
    String password 
) {

}
