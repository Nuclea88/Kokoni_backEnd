package com.example.kokoni.dto.response;

public record UserProfileResponse(
    Long id,
    String username,
    // String avatarUrl, // Opcional, si lo añades a la Entity
    Integer level, 
    String rankName, 
    Integer totalChaptersRead, 
    Integer streakDays, 
    Integer timeReadHours 
) {}
