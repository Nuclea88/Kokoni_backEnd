package com.example.kokoni.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.kokoni.dto.response.MangaDetailResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.User;
import com.example.kokoni.mapper.MangaMapper;
import com.example.kokoni.repository.MangaRepository;
import com.example.kokoni.repository.UserChapterProgressRepository;
import com.example.kokoni.repository.UserMediaTrackerRepository;

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
    private final UserMediaTrackerRepository trackerRepository;
    private final UserChapterProgressRepository progressRepository;
    private final AuthService authService;

    @Override
    public List<MangaSummaryResponse> searchManga(String query, int page) {
        
        List<Manga> rawMangas = mangaProvider.searchManga(query, page);
        
        return rawMangas.stream()
                .map(manga ->{
                    User me = authService.getAuthenticatedUser();
                    boolean isAdded = me != null && trackerRepository.existsByUserIdAndMediaExternalId(me.getId(), manga.getExternalId());
                    return mangaMapper.toSummaryResponse(manga, isAdded);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MangaDetailResponse getMangaDetails(String externalId) {
        Manga manga = searchAndSave(externalId);
        User me = authService.getAuthenticatedUser();
      
        boolean isAddedInTracker = me != null && trackerRepository.existsByUserIdAndMediaId(me.getId(), manga.getId());
        Integer highest = me != null ? progressRepository.findHighestChapterRead(me.getId(), manga.getId()) : 0;
        Integer currentChapter = highest != null ? highest : 0;
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


