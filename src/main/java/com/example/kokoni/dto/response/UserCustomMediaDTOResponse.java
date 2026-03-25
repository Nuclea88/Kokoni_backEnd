package com.example.kokoni.dto.response;

public record UserCustomMediaDTOResponse(
    Long id,
    String title,
    String imageUrl,
    String customAuthor,
    Integer customTotalChapters,
    String customStatus,
    String description
) {}
