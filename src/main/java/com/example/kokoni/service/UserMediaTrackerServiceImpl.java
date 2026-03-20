package com.example.kokoni.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.kokoni.dto.request.AddTrackerRequest;
import com.example.kokoni.dto.request.UpdateTrackerRequest;
import com.example.kokoni.dto.response.TrackerItemResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserMediaTracker;
import com.example.kokoni.mapper.TrackerMapper;
import com.example.kokoni.repository.UserMediaTrackerRepository;
import com.example.kokoni.security.UserDetail;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMediaTrackerServiceImpl implements UserMediaTrackerService {

    private final UserMediaTrackerRepository trackerRepository;
    private final MangaService mangaService; 
    private final UserService userService;
    private final TrackerMapper trackerMapper;

    @Override
    @Transactional
    public TrackerItemResponse addTracker(AddTrackerRequest request) {
        User me = userService.getAuthenticatedUser();
        
        Manga manga = mangaService.searchAndSave(request.externalId());
        if (trackerRepository.existsByUserIdAndMediaId(me.getId(), manga.getId())) {
            throw new RuntimeException("Este manga ya está en tu lista. Usa update para modificarlo.");
        }
        UserMediaTracker tracker = new UserMediaTracker();
        tracker.setUser(me);
        tracker.setMedia(manga);
        tracker.setUserStatus(request.status());
        tracker.setScore(request.score());
        tracker.setCreatedAt(LocalDateTime.now());
        tracker.setUpdatedAt(LocalDateTime.now());
        
        //En el futuro, si el status es COMPLETED, podríamos rellenar finishDate automático.
        UserMediaTracker savedTracker = trackerRepository.save(tracker);
        return trackerMapper.toTrackerItemResponse(savedTracker);
    }

    @Override
    public List<TrackerItemResponse> getMyTrackers() {
        User me = userService.getAuthenticatedUser();
        
        return trackerRepository.findByUserId(me.getId())
                .stream()
                .map(trackerMapper::toTrackerItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrackerItemResponse updateTracker(Long trackerId, UpdateTrackerRequest request) {
        User me = userService.getAuthenticatedUser();
        UserMediaTracker tracker = getMyTrackerOrThrow(trackerId, me);
      
        if (request.status() != null) tracker.setUserStatus(request.status());
        if (request.score() != null) tracker.setScore(request.score());
        if (request.notes() != null) tracker.setNotes(request.notes());
        tracker.setUpdatedAt(LocalDateTime.now());
        return trackerMapper.toTrackerItemResponse(trackerRepository.save(tracker));
    }

    @Override
    @Transactional
    public void deleteTracker(Long trackerId) {
        User me = userService.getAuthenticatedUser();
        UserMediaTracker tracker = getMyTrackerOrThrow(trackerId, me);
        trackerRepository.delete(tracker);
    }
    
    @Override
    public boolean isMangaTrackedByMe(Long mediaId) {
        try {
            User me = userService.getAuthenticatedUser();
            return trackerRepository.existsByUserIdAndMediaId(me.getId(), mediaId);
        } catch (Exception e) {
            return false; 
        }
    }

    @Override
    public boolean isMangaTrackedByExternalId(String externalId) {
        try {
            User me = userService.getAuthenticatedUser();
            
            return trackerRepository.findByUserId(me.getId()).stream()
                .anyMatch(tracker -> tracker.getMedia().getExternalId().equals(externalId));
        } catch (Exception e) {
            return false; 
        }
    }

    @Override
    public UserMediaTracker getTrackerEntityInternal(Long trackerId) {
        User me = userService.getAuthenticatedUser();
        return getMyTrackerOrThrow(trackerId, me);
    }

     private UserMediaTracker getMyTrackerOrThrow(Long trackerId, User me) {
        UserMediaTracker tracker = trackerRepository.findById(trackerId)
            .orElseThrow(() -> new EntityNotFoundException("Tracker no encontrado"));
                
        if (!tracker.getUser().getId().equals(me.getId())) {
            throw new RuntimeException("Acceso denegado: Este tracker no te pertenece.");
        }
        
        return tracker;
    }
}