package com.example.kokoni.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.kokoni.dto.request.AddChapterProgressRequest;
import com.example.kokoni.dto.response.ChapterProgressResponse;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserChapterProgress;
import com.example.kokoni.entity.UserMediaTracker;
import com.example.kokoni.mapper.ChapterProgressMapper;
import com.example.kokoni.repository.UserChapterProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;



//mirarsela con un poco mas de mimo cuando haya cabeza

@Service
@RequiredArgsConstructor
public class UserChapterProgressServiceImpl implements UserChapterProgressService { 

    private final UserChapterProgressRepository progressRepository;
    private final UserMediaTrackerService trackerService;
    private final AuthService authService; 
    private final ChapterProgressMapper progressMapper;

    @Override
    @Transactional
    public ChapterProgressResponse markChapterAsRead(Long trackerId, AddChapterProgressRequest request) {
        
        UserMediaTracker tracker = trackerService.getTrackerEntityInternal(trackerId);
        if (progressRepository.existsByTrackerIdAndProgressUnit(trackerId, request.progressUnit())) {
            throw new RuntimeException("Ese capítulo ya está marcado como leído.");
        }
        UserChapterProgress progress = progressMapper.toEntity(request);
        progress.setTracker(tracker);
       
        UserChapterProgress saved = progressRepository.save(progress); 
        return progressMapper.toResponse(saved); 
    }

    @Override
    public List<ChapterProgressResponse> getReadChapters(Long trackerId) {
        trackerService.getTrackerEntityInternal(trackerId); 
        
        return progressRepository.findByTrackerId(trackerId).stream()
            .map(progressMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void unmarkChapterAsRead(Long trackerId, Integer progressUnit) {
        trackerService.getTrackerEntityInternal(trackerId); 
        progressRepository.deleteByTrackerIdAndProgressUnit(trackerId, progressUnit);
    }
    
    @Override
    public Integer getCurrentChapterForManga(Long mediaId) {
        try {
            User me = authService.getAuthenticatedUser();
            Integer highest = progressRepository.findHighestChapterRead(me.getId(), mediaId);
            return highest != null ? highest : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}