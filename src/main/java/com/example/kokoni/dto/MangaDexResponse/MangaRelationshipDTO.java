package com.example.kokoni.dto.MangaDexResponse;

import java.util.Map;

public record MangaRelationshipDTO(
        String id,
        String type,
        Map<String, Object> attributes
    ) {}