package com.example.kokoni.dto;

import java.util.Map;

public record MangaRelationshipDTO(
        String id,
        String type,
        Map<String, Object> attributes
    ) {}