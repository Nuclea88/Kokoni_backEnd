package com.example.kokoni.external;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.kokoni.dto.kitsuResponse.KitsuAttributesDTO;
import com.example.kokoni.dto.kitsuResponse.KitsuDataDTO;
import com.example.kokoni.dto.kitsuResponse.KitsuResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.MediaTitle;
import com.example.kokoni.service.MangaMetadataEnricher;

@Component
public class KitsuExternal implements MangaMetadataEnricher {
private final WebClient webClient;

    public KitsuExternal(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://kitsu.io/api/edge").build();
    }

    @Override
    public void enrich(Manga manga) {
        if (manga.getTotalChapters() != null && manga.getTotalChapters() > 0) return;

        List<String> titlesToTry = extractTitles(manga);

    for (String title : titlesToTry) {
        Optional<KitsuDataDTO> kitsuMatch = fetchBestMatch(title, manga.getAuthor());
        
        if (kitsuMatch.isPresent()) {
            var attrs = kitsuMatch.get().attributes();
            manga.setTotalChapters(attrs.chapterCount());
            manga.setTotalVolumes(attrs.volumeCount());
            return; 
        }
    }
}

private List<String> extractTitles(Manga manga) {
        return manga.getTitles().stream()
            .sorted(Comparator.comparing(t -> {
                String lang = t.getLanguageCode();
                if ("en".equals(lang)) return 1;
                if ("ja-ro".equals(lang)) return 2;
                return 3;
            }))
            .map(MediaTitle::getTitle)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
    }

private Optional<KitsuDataDTO> fetchBestMatch(String title, String expectedAuthor) {
    try {
        // 1. Pedimos a Kitsu que nos incluya los autores en la respuesta (include=staff o characters es lento, mejor validamos por texto)
        KitsuResponse response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/manga")
                .queryParam("filter[text]", title)
                .build())
            .retrieve()
            .bodyToMono(KitsuResponse.class)
            .block();

        if (response == null || response.data().isEmpty()) return Optional.empty();

        // 2. VALIDACIÓN DE SEGURIDAD
        return response.data().stream()
            .filter(data -> isReliableMatch(data.attributes(), title, expectedAuthor))
            .findFirst();

    } catch (Exception e) {
        return Optional.empty();
    }
}

private boolean isReliableMatch(KitsuAttributesDTO kitsuAttr, String searchTitle, String expectedAuthor) {
    // Si el título es idéntico, ya tenemos un punto.
    boolean titleMatch = kitsuAttr.canonicalTitle().equalsIgnoreCase(searchTitle);
    return titleMatch; 
}







    public Optional<KitsuAttributesDTO> fetchMetadataByTitle(String title) {
        try {
            KitsuResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/manga")
                    .queryParam("filter[text]", title)
                    .build())
                .retrieve()
                .bodyToMono(KitsuResponse.class)
                .block();

            if (response != null && !response.data().isEmpty()) {
                // Devolvemos los atributos del primer resultado
                return Optional.of(response.data().get(0).attributes());
            }
        } catch (Exception e) {
            // Loguear error pero no romper el flujo, MangaDex ya dio algo de info
        }
        return Optional.empty();
    }
}