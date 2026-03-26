package com.example.kokoni.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.kokoni.dto.response.ChapterProgressResponse;
import com.example.kokoni.dto.response.MangaDetailResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.entity.UserMediaTracker;
import com.example.kokoni.mapper.ChapterProgressMapper;
import com.example.kokoni.mapper.MangaMapper;
import com.example.kokoni.repository.MangaRepository;
import com.example.kokoni.repository.UserChapterProgressRepository;
import com.example.kokoni.repository.UserCustomMediaRepository;
import com.example.kokoni.repository.UserMediaTrackerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MangaServiceImpl implements MangaService {

    private final MangaProvider mangaProvider;
    private final MangaRepository mangaRepository;
    private final List<MangaMetadataEnricher> enrichers;
    private final MangaMapper mangaMapper;
    private final ChapterProgressMapper chapterProgressMapper;
    private final UserMediaTrackerRepository trackerRepository;
    private final UserChapterProgressRepository progressRepository;
    private final AuthService authService;
    private final UserCustomMediaRepository customMediaRepository;

    @Override
    public List<MangaSummaryResponse> searchManga(String query, int page) {

        List<Manga> rawMangas = mangaProvider.searchManga(query, page);

        return rawMangas.stream()
                .map(manga -> {
                    User me = authService.getOptionalAuthenticatedUser();
                    boolean isAdded = me != null
                            && trackerRepository.existsByUserIdAndMediaExternalId(me.getId(), manga.getExternalId());
                    return mangaMapper.toSummaryResponse(manga, isAdded);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MangaDetailResponse getMangaDetails(String externalId) {

        Optional<Manga> existingManga = findMangaByExternalId(externalId);
        Manga manga= existingManga.orElseGet(() -> getFullDetails(externalId));
        Long trackerId = null;
        List<ChapterProgressResponse> readChapters = new java.util.ArrayList<>();
        Integer currentChapter = 0;
        boolean isAddedInTracker = false;
        User me = authService.getOptionalAuthenticatedUser();

        if (existingManga.isPresent()) {
            manga = existingManga.get();

            if (me != null) {

                Optional<UserCustomMedia> customOpt = customMediaRepository.findByCreatorIdAndBaseMangaId(me.getId(),
                        manga.getId());
                if (customOpt.isPresent()) {
                    UserCustomMedia custom = customOpt.get();
                    if (custom.getCustomTotalChapters() != null)
                        manga.setTotalChapters(custom.getCustomTotalChapters());
                    if (custom.getCustomAuthor() != null)
                        manga.setAuthor(custom.getCustomAuthor());
                    if (custom.getImageUrl() != null)
                        manga.setImageUrl(custom.getImageUrl());
                    if (custom.getDescription() != null)
                        manga.setDescription(custom.getDescription());
                }

                Optional<UserMediaTracker> trackerOpt = trackerRepository.findByUserIdAndMediaId(me.getId(),
                        manga.getId());
                if (trackerOpt.isPresent()) {
                    UserMediaTracker tracker = trackerOpt.get();
                    isAddedInTracker = true;
                    trackerId = tracker.getId();

                    readChapters = progressRepository.findByTrackerId(trackerId).stream()
                            .map(chapterProgressMapper::toResponse).toList();
                    Integer highest = progressRepository.findHighestChapterRead(me.getId(), manga.getId());
                    currentChapter = (highest != null) ? highest : 0;
                }
            }
        } else {
            manga = getFullDetails(externalId);
        }
        return mangaMapper.toDetailResponse(manga, isAddedInTracker, currentChapter, trackerId, readChapters);
    }

    @Override
    public Optional<Manga> findMangaByExternalId(String externalId) {
        return mangaRepository.findByExternalId(externalId);
    }

    @Override
    public Manga findById(Long id) {
        return mangaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manga no encontrado"));
    }

    @Override
    @Transactional
    public Manga searchAndSave(String externalId) {

        Optional<Manga> existing = findMangaByExternalId(externalId);
        if (existing.isPresent()) {
            return existing.get();
        }
        Manga mangaToSave = getFullDetails(externalId);

        try {
            return mangaRepository.save(mangaToSave);
        } catch (Exception e) {
            return findMangaByExternalId(externalId).orElseThrow();
        }
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
