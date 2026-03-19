package com.example.kokoni.dto;

public record MangaDTO(
    Long id,
    String title,
    String imageUrl,
    String author,
    Integer totalChapters,
    String status
) {
}
