package com.example.kokoni.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.kokoni.entity.Manga;
import com.example.kokoni.repository.MangaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MangaServiceImpl implements MangaService{

    private final MangaProvider mangaProvider;
    private final MangaRepository mangaRepository;
    private final List<MangaMetadataEnricher> enrichers;

    
    @Override
    public List<Manga> searchInApi(String query, int page) {
        return mangaProvider.searchManga(query, page);
    }

    @Override
    public Optional<Manga> findMangaByExternalId(String externalId) {
    return mangaRepository.findByExternalId(externalId);
    }

//     @Override
//     public Manga getMangaByExternalId(String externalId) {
//     return findMangaByExternalId(externalId)
//         .orElseThrow(() -> new EntityNotFoundException("Manga con ID externa " + externalId + " no encontrado en tu lista"));
// }

    @Transactional
    public Manga searchAndSave(String externalId) {
    
    return findMangaByExternalId(externalId)
        .orElseGet(() -> {
            Manga mangaToSave = getFullDetails(externalId);
                return mangaRepository.save(mangaToSave);
        });
    }
    public void enrichData(Manga manga) {
        if (enrichers != null) {
            enrichers.forEach(enricher -> enricher.enrich(manga));
        }
    }

    public Manga getFullDetails(String externalId) {

        return mangaRepository.findByExternalId(externalId)
            .orElseGet(() -> {

                Manga mangaFromApi = mangaProvider.searchByExternalId(externalId);
                enrichData(mangaFromApi);
                return mangaFromApi;
            });
    }

}


