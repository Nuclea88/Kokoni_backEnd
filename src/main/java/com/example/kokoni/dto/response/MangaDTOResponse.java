package com.example.kokoni.dto.response;

public record MangaDTOResponse(
    Long id,
    String title,
    String imageUrl,
    String author,
    Integer totalChapters,
    String status
) {
}
