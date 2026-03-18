package com.example.kokoni.external;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.kokoni.dto.response.MangaDexResponse;
import com.example.kokoni.dto.response.MangaDexSingleResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.mapper.MangaDexMapper;
import com.example.kokoni.service.MangaProvider;

import jakarta.persistence.EntityNotFoundException;

@Component
public class MangaDexExternal implements MangaProvider {
   private final WebClient webClient;
   private final MangaDexMapper mangaMapper;

   public MangaDexExternal(WebClient webClient, MangaDexMapper mangaMapper){
        this.webClient = webClient;
        this.mangaMapper = mangaMapper;
   }
    @Override
    public List<Manga> searchManga(String query, int page) {
        int limit = 20;
        int offset = page*limit;
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/manga")
                .queryParam("title", query)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("contentRating[]", List.of("safe", "suggestive", "erotica", "pornographic"))
                .queryParam("includes[]", "cover_art")
                .build())
            .retrieve()
            .bodyToMono(MangaDexResponse.class) 
            .block() // Como tu app no es 100% reactiva, usamos .block() para esperar el resultado
            .data()
            .stream()
            .map(mangaMapper::toEntity)
            .toList();
    }
    
   @Override
public Manga searchByExternalId(String externalId) {
    MangaDexSingleResponse response = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/manga/{id}")
            .queryParam("includes[]", "cover_art")
            .queryParam("includes[]", "author")
            .build(externalId))
        .retrieve()
        .bodyToMono(MangaDexSingleResponse.class)
        .block();

    if (response == null || response.data() == null) {
        throw new EntityNotFoundException("No se encontró el manga en la API externa");
    }

    // EXTRAEMOS el DTO y lo pasamos al mapper directamente
    return mangaMapper.toEntity(response.data()); // Sin .map(), es una llamada directa
}
}