package com.example.kokoni.dto.kitsuResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KitsuAttributesDTO(
    String canonicalTitle,
    @JsonProperty("chapterCount") Integer chapterCount,
    @JsonProperty("volumeCount") Integer volumeCount,
    String status
) {}
