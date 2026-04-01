package com.example.kokoni.dto.request;

import com.example.kokoni.entity.enums.UserStatus;

import jakarta.validation.constraints.NotBlank;

public record UserCustomMediaDTORequest(
    @NotBlank(message = "El título es obligatorio")
    String title, 
    String description,
    String imageUrl,
    String customAuthor,
    Integer customTotalChapters,
    Long baseMangaId,
    UserStatus status
) {

}
