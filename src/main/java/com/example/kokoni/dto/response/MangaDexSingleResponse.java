package com.example.kokoni.dto.response;

import com.example.kokoni.dto.MangaDexResponse.MangaDexDataDTO;

public record MangaDexSingleResponse(
    String result,
    MangaDexDataDTO data 
)
{}
