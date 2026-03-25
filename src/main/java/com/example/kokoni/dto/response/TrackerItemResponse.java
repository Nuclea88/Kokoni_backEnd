package com.example.kokoni.dto.response;

public record TrackerItemResponse(
    Long trackerId,
    Long mangaId,
    String mangaTitle,
    String mangaImageUrl,
    String mangaStatus, 
    String userStatus, 
    Integer progressUnit, 
    Integer totalChapters, 
    Double progressPercentage,
    String externalId  
) {

}
