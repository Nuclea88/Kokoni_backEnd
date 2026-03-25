package com.example.kokoni.service;

import java.util.List;
import java.util.Optional;

import com.example.kokoni.dto.response.MangaDetailResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.entity.Manga;

public interface MangaService {

    List<MangaSummaryResponse> searchManga(String query, int page);

    MangaDetailResponse getMangaDetails(String externalId);

    Optional<Manga> findMangaByExternalId(String externalId);
    
    Manga findById(Long id);

    Manga searchAndSave(String externalId);
    
    void enrichData(Manga manga);
    
    Manga getFullDetails(String externalId);

}
