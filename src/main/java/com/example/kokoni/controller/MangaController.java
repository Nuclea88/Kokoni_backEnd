package com.example.kokoni.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.kokoni.dto.response.MangaDetailResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.service.MangaService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/mangas")
@RequiredArgsConstructor
public class MangaController {

    private final MangaService mangaService;

    @GetMapping("/search")
    public ResponseEntity<List<MangaSummaryResponse>> searchMangas(@RequestParam String title, @RequestParam(defaultValue = "1") int page) {
        List<MangaSummaryResponse> results = mangaService.searchManga(title, page);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/{externalId}")
    public ResponseEntity<MangaDetailResponse> getMangaDetails(@PathVariable String externalId) {
        MangaDetailResponse details = mangaService.getMangaDetails(externalId);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}