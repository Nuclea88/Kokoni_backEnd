package com.example.kokoni.service;
import java.util.List;
import com.example.kokoni.dto.request.AddTrackerRequest;
import com.example.kokoni.dto.request.UpdateTrackerRequest;
import com.example.kokoni.dto.response.TrackerItemResponse;

public interface UserMediaTrackerService {

    TrackerItemResponse addTracker(AddTrackerRequest request);

    List<TrackerItemResponse> getMyTrackers();

    TrackerItemResponse updateTracker(Long trackerId, UpdateTrackerRequest request);

    void deleteTracker(Long trackerId);
    
    boolean isMangaTrackedByMe(Long mediaId);

    boolean isMangaTrackedByExternalId(String externalId);
}