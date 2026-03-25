package com.example.kokoni.dto.response;

public record CustomListSummaryResponse(
    Long id,
    String name,
    Boolean isPublic,
    Integer itemCount
) {}
