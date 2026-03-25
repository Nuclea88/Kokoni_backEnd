package com.example.kokoni.service;

import java.util.List;

import com.example.kokoni.entity.Manga;

public interface MangaProvider {

    List<Manga> searchManga(String query, int page);

    Manga searchByExternalId(String externalId);
}
