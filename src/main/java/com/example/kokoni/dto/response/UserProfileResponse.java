package com.example.kokoni.dto.response;

public record UserProfileResponse(
    Long id,
    String username,
    String avatarUrl, 
    Integer level, 
    String rankName, 
    Integer totalChaptersRead, 
    Integer streakDays, 
    Integer timeReadHours 
) {}
