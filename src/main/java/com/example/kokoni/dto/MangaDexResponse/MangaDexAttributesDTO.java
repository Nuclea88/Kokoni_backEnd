package com.example.kokoni.dto.mangaDexResponse;

import java.util.List;
import java.util.Map;

public record MangaDexAttributesDTO(
        Map<String, String> title,
        List<Map<String, String>> altTitles,
        Map<String, String> description,
        String status,
        String lastChapter,
        String lastVolume,
        String contentRating,
        Integer year,      
        String originalLanguage,
        LinksDTO links
    ) {}