package com.example.kokoni.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomListRequest(
    @NotBlank(message = "El nombre de la lista no puede estar vacío")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    String name,
    Boolean isPublic
) { 
}
