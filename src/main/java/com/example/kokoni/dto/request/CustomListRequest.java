package com.example.kokoni.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CustomListRequest(
    @NotBlank(message = "El nombre de la lista no puede estar vacío")
    String name,
    Boolean isPublic
) { 
}
