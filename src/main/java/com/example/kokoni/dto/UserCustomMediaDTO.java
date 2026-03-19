package com.example.kokoni.dto;

public record UserCustomMediaDTO(
    Long id,
    String title,
    String imageUrl,
    String customAuthor,
    Integer customTotalChapters,
    String customStatus
) {}
