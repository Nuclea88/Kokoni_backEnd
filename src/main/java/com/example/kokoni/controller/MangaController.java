package com.example.kokoni.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kokoni.entity.Manga;
import com.example.kokoni.service.MangaService;

@RestController
@RequestMapping("/api/v1/manga")
public class MangaController {

    private final MangaService mangaService;

    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Manga>> search(@RequestParam String title, int page) {
        // Llama al servicio, que llama al adaptador, que llama a MangaDex...
        List<Manga> results = mangaService.searchInApi(title, page);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/save/{externalId}")
    public ResponseEntity<Manga> saveManga(@PathVariable String externalId) {
        Manga savedManga = mangaService.searchAndSave(externalId);
        
        return new ResponseEntity<>(savedManga, HttpStatus.CREATED);
    }
}