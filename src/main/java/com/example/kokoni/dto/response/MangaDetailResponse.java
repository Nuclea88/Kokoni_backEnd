package com.example.kokoni.dto.response;

import java.util.List;
import java.util.Map;

public record MangaDetailResponse(
    
    Long id,
    String title,
    String author,
    String imageUrl,
    Double averageScore,
    Integer rankPosition, 
    Integer readersCount, 
    String status, 
    Map<String, String> description,
    List<String> genres,
    Integer totalChapters,
    Integer currentChapter,
    Boolean isAddedInTracker,
    Long trackerId,
    String userStatus,
    List<ChapterProgressResponse> readChapters
) {}
