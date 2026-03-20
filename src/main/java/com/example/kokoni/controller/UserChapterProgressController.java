package com.example.kokoni.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.kokoni.dto.request.AddChapterProgressRequest;
import com.example.kokoni.dto.response.ChapterProgressResponse;
import com.example.kokoni.service.UserChapterProgressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trackers/{trackerId}/progress")
@RequiredArgsConstructor
public class UserChapterProgressController {

    private final UserChapterProgressService progressService;

    @GetMapping
    public ResponseEntity<List<ChapterProgressResponse>> getReadChapters(@PathVariable Long trackerId) {
        List<ChapterProgressResponse> readedChapters = progressService.getReadChapters(trackerId);
        return new ResponseEntity<>(readedChapters, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ChapterProgressResponse> markAsRead(
            @PathVariable Long trackerId, 
            @Valid @RequestBody AddChapterProgressRequest request) {
               ChapterProgressResponse marked = progressService.markChapterAsRead(trackerId, request);
        return new ResponseEntity<>(marked, HttpStatus.CREATED);
    }

    @DeleteMapping("/{progressUnit}")
    public ResponseEntity<Void> unmarkAsRead(
            @PathVariable Long trackerId, 
            @PathVariable Integer progressUnit) {
        progressService.unmarkChapterAsRead(trackerId, progressUnit);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}