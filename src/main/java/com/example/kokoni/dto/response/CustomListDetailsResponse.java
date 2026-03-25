package com.example.kokoni.dto.response;

import java.util.List;

public record CustomListDetailsResponse(
    Long id,
    String name,
    Boolean isPublic,
    List<MangaSummaryResponse> items
) {}
