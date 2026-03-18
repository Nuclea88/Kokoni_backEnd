package com.example.kokoni.dto.response;

import java.util.List;
import java.util.Map;

import com.example.kokoni.dto.MangaDexResponse.MangaDexDataDTO;


public record MangaDexResponse(List<MangaDexDataDTO> data) {
}