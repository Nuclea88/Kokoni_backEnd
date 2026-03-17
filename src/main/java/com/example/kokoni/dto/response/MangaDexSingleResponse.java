package com.example.kokoni.dto.response;

import com.example.kokoni.dto.MangaDataDTO;

public record MangaDexSingleResponse(
    String result,
    MangaDataDTO data 
)
{}
