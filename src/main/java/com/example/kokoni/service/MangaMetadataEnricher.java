package com.example.kokoni.service;

import com.example.kokoni.entity.Manga;

public interface MangaMetadataEnricher {
    void enrich(Manga manga);
}
