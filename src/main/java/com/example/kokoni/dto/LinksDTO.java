package com.example.kokoni.dto;

public record LinksDTO(
        String al,    // AniList ID
        String mu,    // MangaUpdates ID
        String raw,   // Link original coreano
        String engtl  // Link oficial en inglés (Lezhin)
    ) {}