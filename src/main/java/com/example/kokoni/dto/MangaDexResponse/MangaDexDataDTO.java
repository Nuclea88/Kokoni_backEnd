package com.example.kokoni.dto.MangaDexResponse;

import java.util.List;

public record MangaDexDataDTO(
        String id,
        String type,
        MangaDexAttributesDTO attributes,
        List<MangaRelationshipDTO> relationships
    ) {}

