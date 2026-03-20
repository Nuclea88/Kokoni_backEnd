package com.example.kokoni.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.kokoni.dto.request.AddTrackerRequest;
import com.example.kokoni.dto.request.UpdateTrackerRequest;
import com.example.kokoni.dto.response.TrackerItemResponse;
import com.example.kokoni.service.UserMediaTrackerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trackers")
@RequiredArgsConstructor
public class UserMediaTrackerController {

    private final UserMediaTrackerService trackerService;
    
    @GetMapping
    public ResponseEntity<List<TrackerItemResponse>> getMyTrackers() {
        List<TrackerItemResponse> myTrackers =trackerService.getMyTrackers();
        return new ResponseEntity<>(myTrackers,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrackerItemResponse> addTracker(@Valid @RequestBody AddTrackerRequest request) {
        TrackerItemResponse tracker = trackerService.addTracker(request);
        return new ResponseEntity<>(tracker, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TrackerItemResponse> updateTracker(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateTrackerRequest request) {

            TrackerItemResponse trackerUpdated = trackerService.updateTracker(id, request);
        return new ResponseEntity<>(trackerUpdated, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTracker(@PathVariable Long id) {
        trackerService.deleteTracker(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}