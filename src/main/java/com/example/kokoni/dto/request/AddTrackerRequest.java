package com.example.kokoni.dto.request;

import com.example.kokoni.entity.enums.UserStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddTrackerRequest(
    @NotBlank(message = "ID externo obligatorio")
    String externalId,    

    @NotNull(message = "El estado es obligatorio")
    UserStatus status,    

    @Min(value = 0, message = "Puntuación mínima 0")
    @Max(value = 10, message = "Puntuación máxima 10")
    Integer score 
) {

}
