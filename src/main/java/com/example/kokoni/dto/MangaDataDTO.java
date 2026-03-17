package com.example.kokoni.dto;

import java.util.List;

public record MangaDataDTO(
        String id,
        String type,
        MangaAttributesDTO attributes,
        List<MangaRelationshipDTO> relationships
    ) {}

