package com.example.kokoni.dto.response;

import java.util.List;

public record MangaDetailResponse(
    
    Long id,
    String title,
    String author,
    String imageUrl,
    Double averageScore,
    Integer rankPosition, 
    Integer readersCount, 
    String status, 
    String description,
    List<String> genres,
    Integer totalChapters,
    
    Integer currentChapter 
) {}
