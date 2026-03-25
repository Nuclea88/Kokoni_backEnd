package com.example.kokoni.dto.response;

import java.util.List;

public record MangaSummaryResponse(
        Long id,
        String externalId,
        String title,
        String author,
        String imageUrl,
        Double averageScore,
        List<String> genres,
        Boolean isAddedToLibrary) {

}
