package com.example.kokoni.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.kokoni.dto.response.MangaDetailResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.mapper.MangaMapper;
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
    private final MangaMapper mangaMapper;

    @Override
    public List<MangaSummaryResponse> searchManga(String query, int page) {
        
        List<Manga> rawMangas = mangaProvider.searchManga(query, page);
        
        // Mapeamos a DTO (Asumimos falso para "isAdded" de momento hasta que hagamos el Tracker)
        return rawMangas.stream()
                .map(manga -> mangaMapper.toSummaryResponse(manga, false))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MangaDetailResponse getMangaDetails(String externalId) {
        Manga manga = searchAndSave(externalId);
        //  Cuando implementemos el Tracker, aquí calcularemos si el AuthUser lo tiene añadido.
        boolean isAddedInTracker = false; 
        Integer currentChapter = 0;
        return mangaMapper.toDetailResponse(manga, isAddedInTracker, currentChapter);
    }

    @Override
    public Optional<Manga> findMangaByExternalId(String externalId) {
    return mangaRepository.findByExternalId(externalId);
    }

    @Override
    public Manga findById(Long id){
        return mangaRepository.findById(id)
        .orElseThrow(()-> new EntityNotFoundException("Manga no encontrado"));
    }

    @Override
    @Transactional
    public Manga searchAndSave(String externalId) {
    
    return findMangaByExternalId(externalId)
        .orElseGet(() -> {
            Manga mangaToSave = getFullDetails(externalId);
                return mangaRepository.save(mangaToSave);
        });
    }
    @Override
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


