package com.example.kokoni.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddChapterProgressRequest(
    @NotNull(message = "La unidad de progreso es obligatoria")
    @Min(value = 0, message = "El progreso no puede ser negativo")
    Integer progressUnit, 
    
    Integer subUnit,    
    Boolean isSpecial,
    String reaction  
) {

}
