package com.example.kokoni.service;

import java.util.List;
import java.util.Optional;

import com.example.kokoni.entity.Manga;

public interface MangaService {

    List<Manga> searchInApi(String query, int page);

    Optional<Manga> findMangaByExternalId(String externalId);

    Manga searchAndSave(String externalId);
    
    Manga getMangaByExternalId(String externalId);
}
