package com.example.kokoni.dto.request;

public record UserCustomMediaDTORequest(
    String title, 
    String description,
    String imageUrl,
    String customAuthor,
    Integer customTotalChapters,
    Long baseMangaId
) {

}
