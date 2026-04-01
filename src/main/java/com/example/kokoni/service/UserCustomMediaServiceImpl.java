package com.example.kokoni.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.kokoni.dto.request.UserCustomMediaDTORequest;
import com.example.kokoni.dto.response.ChapterProgressResponse;
import com.example.kokoni.dto.response.UserCustomMediaDTOResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.MediaTitle;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.entity.UserMediaTracker;
import com.example.kokoni.exception.TitleException;
import com.example.kokoni.mapper.ChapterProgressMapper;
import com.example.kokoni.mapper.UserCustomMediaMapper;
import com.example.kokoni.repository.UserChapterProgressRepository;
import com.example.kokoni.repository.UserCustomMediaRepository;
import com.example.kokoni.repository.UserMediaTrackerRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCustomMediaServiceImpl implements UserCustomMediaService {
    private final UserCustomMediaRepository repository;
    private final MangaService mangaService;
    private final UserCustomMediaMapper customMediaMapper;
    private final AuthService authService;
    private final UserMediaTrackerRepository trackerRepository;
    private final UserChapterProgressRepository progressRepository;
    private final ChapterProgressMapper chapterProgressMapper;

    private UserCustomMedia getCustomMediaOwnedByMeOrThrow(Long id) {
        User me = authService.getAuthenticatedUser();
        UserCustomMedia media = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Media personalizado no encontrado"));

        if (!media.getCreator().getId().equals(me.getId())) {
            throw new RuntimeException("Acceso denegado: No puedes modificar obras de otros usuarios.");
        }
        return media;
    }

    @Override
    @Transactional
    public UserCustomMediaDTOResponse createCustom(UserCustomMediaDTORequest request) {

        UserCustomMedia custom = customMediaMapper.toEntity(request);

        User creator = authService.getAuthenticatedUser();
        custom.setCreator(creator);
        custom.setProvider("USER_CUSTOM");

        if (request.baseMangaId() != null) {
            Manga base = mangaService.findById(request.baseMangaId());
            custom.setBaseManga(base);
            custom.setImageUrl(request.imageUrl() != null ? request.imageUrl() : base.getImageUrl());
            custom.setDescription(request.description() != null ? request.description() : base.getDescription());

            

            for (MediaTitle baseTitle : base.getTitles()) {
                if (!baseTitle.getIsPrimary()) {
                    MediaTitle secTitle = new MediaTitle();
                    secTitle.setTitle(baseTitle.getTitle());
                    secTitle.setLanguageCode(baseTitle.getLanguageCode());
                    secTitle.setIsPrimary(false);
                    secTitle.setMedia(custom);
                    custom.addTitle(secTitle);
                }
            }
        }
        MediaTitle primaryTitle = new MediaTitle();
            primaryTitle.setTitle(request.title());
            primaryTitle.setLanguageCode("custom");
            primaryTitle.setIsPrimary(true);
            primaryTitle.setMedia(custom);
            custom.addTitle(primaryTitle);

        UserCustomMedia saved = repository.save(custom);
        if (request.baseMangaId() != null) {
            var trackerOpt = trackerRepository.findByUserIdAndMediaId(creator.getId(), request.baseMangaId());
            if (trackerOpt.isPresent()) {
                UserMediaTracker tracker = trackerOpt.get();
                tracker.setMedia(saved); // "Desvinculamos" del oficial y lo pegamos al tuyo
                trackerRepository.save(tracker);
            } else if (request.status() != null) {
                // Es un manga creado dsd 0 (sin base)
                createNewTracker(creator, saved, request.status());
            }
        }
        return customMediaMapper.toResponse(saved);
    }

    // for (MediaTitle baseTitle : base.getTitles()) {
    // if (!baseTitle.getIsPrimary()) {
    // MediaTitle secTitle = new MediaTitle();
    // secTitle.setTitle(baseTitle.getTitle());
    // secTitle.setLanguageCode(baseTitle.getLanguageCode());
    // secTitle.setIsPrimary(false);
    // secTitle.setMedia(custom);
    // custom.addTitle(secTitle);
    // }
    // }
    // }

    // for (MediaTitle baseTitle : base.getTitles()) {
    // MediaTitle newTitle = new MediaTitle();
    // newTitle.setTitle(baseTitle.getTitle());
    // newTitle.setLanguageCode(baseTitle.getLanguageCode());
    // newTitle.setIsPrimary(baseTitle.getIsPrimary());
    // newTitle.setMedia(custom);
    // custom.addTitle(newTitle);
    // }
    // // }
    // else {
    // if (custom.getTitles() == null || request.title().isBlank()) {
    // throw new TitleException("Debe tener al menos un título.");
    // }
    // MediaTitle newTitle = new MediaTitle();
    // newTitle.setTitle(request.title());
    // newTitle.setLanguageCode("es");
    // newTitle.setIsPrimary(true);
    // newTitle.setMedia(custom);
    // custom.addTitle(newTitle);
    // }
    // UserCustomMedia saved = repository.save(custom);
    // if (request.status() != null) {
    // UserMediaTracker tracker = new UserMediaTracker();
    // tracker.setUser(creator);
    // tracker.setMedia(saved);
    // tracker.setUserStatus(request.status());
    // tracker.setScore(0);
    // tracker.setCreatedAt(LocalDateTime.now());
    // tracker.setUpdatedAt(LocalDateTime.now());
    // trackerRepository.save(tracker);
    // }
    // return customMediaMapper.toResponse(saved);
    // }

    @Override
    public UserCustomMediaDTOResponse searchById(Long id) {

        UserCustomMedia media = getCustomMediaOwnedByMeOrThrow(id);
        User me = authService.getAuthenticatedUser();

        if ((media.getDescription() == null || media.getDescription().isBlank()) && media.getBaseManga() != null) {
            media.setDescription(media.getBaseManga().getDescription());
        }
        if ((media.getImageUrl() == null || media.getImageUrl().isBlank()) && media.getBaseManga() != null) {
            media.setImageUrl(media.getBaseManga().getImageUrl());
        }

        boolean isAddedInTracker = false;
        Long trackerId = null;
        String userStatus = null;
        List<ChapterProgressResponse> readChapters = new java.util.ArrayList<>();
        Integer currentChapter = 0;
        var trackerOpt = trackerRepository.findByUserIdAndMediaId(me.getId(), id);
        if (trackerOpt.isPresent()) {
            UserMediaTracker tracker = trackerOpt.get();
            isAddedInTracker = true;
            trackerId = tracker.getId();
            userStatus = tracker.getUserStatus().name();

            readChapters = progressRepository.findByTrackerId(trackerId).stream()
                    .map(chapterProgressMapper::toResponse).toList();

            Integer highest = progressRepository.findHighestChapterRead(me.getId(), media.getId());
            currentChapter = (highest != null) ? highest : 0;
        }
        return customMediaMapper.toResponse(media, isAddedInTracker, trackerId, readChapters, currentChapter, userStatus);
    }

    @Override
    @Transactional
    public UserCustomMediaDTOResponse update(Long id, UserCustomMediaDTORequest request) {
        UserCustomMedia existing = getCustomMediaOwnedByMeOrThrow(id);

        if (request.title() != null && !request.title().isBlank()) {
            existing.getTitles().stream()
                    .filter(MediaTitle::getIsPrimary)
                    .findFirst()
                    .ifPresent(t -> t.setTitle(request.title()));
        }
        if (request.customTotalChapters() != null)
            existing.setCustomTotalChapters(request.customTotalChapters());
        if (request.customAuthor() != null)
            existing.setCustomAuthor(request.customAuthor());
        if (request.description() != null)
            existing.setDescription(request.description());
        if (request.imageUrl() != null)
            existing.setImageUrl(request.imageUrl());

        UserCustomMedia saved = repository.save(existing);
        return customMediaMapper.toResponse(saved);
    }

    private void createNewTracker(User creator, UserCustomMedia media,
            com.example.kokoni.entity.enums.UserStatus status) {
        UserMediaTracker tracker = new UserMediaTracker();
        tracker.setUser(creator);
        tracker.setMedia(media);
        tracker.setUserStatus(status);
        tracker.setScore(0);
        tracker.setCreatedAt(LocalDateTime.now());
        tracker.setUpdatedAt(LocalDateTime.now());
        trackerRepository.save(tracker);
    }

    // customMediaMapper.updateEntity(request, existing);
    // if (request.title() != null && !request.title().isBlank()) {
    // existing.getTitles().stream()
    // .filter(MediaTitle::getIsPrimary)
    // .findFirst()
    // .ifPresent(t -> t.setTitle(request.title()));
    // }
    // UserCustomMedia saved = repository.save(existing);
    // return customMediaMapper.toResponse(saved);
    // }

    @Override
    @Transactional
    public void delete(Long id) {
        UserCustomMedia media = getCustomMediaOwnedByMeOrThrow(id);
        repository.delete(media);
    }

    @Override
    public UserCustomMedia findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Media personalizado no encontrado"));
    }
}
