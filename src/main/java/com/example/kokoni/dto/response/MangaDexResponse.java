package com.example.kokoni.dto.response;

import java.util.List;
import java.util.Map;

import com.example.kokoni.dto.MangaDataDTO;


public record MangaDexResponse(List<MangaDataDTO> data) {
}