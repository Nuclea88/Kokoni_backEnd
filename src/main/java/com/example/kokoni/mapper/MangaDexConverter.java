package com.example.kokoni.mapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.example.kokoni.dto.MangaDexResponse.MangaDexDataDTO;
import com.example.kokoni.dto.MangaDexResponse.MangaRelationshipDTO;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.MediaTitle;
import com.example.kokoni.entity.enums.MediaStatus;

@Component
public class MangaDexConverter {

    @Named("extractAuthor")
    public String extractAuthor(List<MangaRelationshipDTO> relationships) {
        if (relationships == null) return null;
        
        return relationships.stream()
            .filter(rel -> "author".equals(rel.type()))
            .filter(rel -> rel.attributes() != null)
            .map(rel -> (String) rel.attributes().get("name"))
            .findFirst()
            .orElse(null);
    }

    @Named("extractCoverUrl")
    public String extractCoverUrl(MangaDexDataDTO dto) {
        if (dto.relationships() == null) return null;
        return dto.relationships().stream()
                .filter(rel -> "cover_art".equals(rel.type()) && rel.attributes() != null)
                .findFirst()
                .map(rel -> {
                    String fileName = (String) rel.attributes().get("fileName");
                    return "https://uploads.mangadex.org/covers/" + dto.id() + "/" + fileName + ".256.jpg";
                })
                .orElse(null);
    }

    @Named("mapDescription")
    public String mapDescription(Map<String, String> desc) {
        if (desc == null) return "Sin descripción";
        return desc.getOrDefault("es", desc.getOrDefault("en", "Sin descripción"));
    }

    @Named("parseSafeInt")
    public Integer parseSafeInt(String value) {
        if (value == null || value.isBlank() || value.equalsIgnoreCase("null")) return null;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public LocalDate mapYear(Integer year) {
        return (year != null) ? LocalDate.of(year, 1, 1) : null;
    }

    public MediaStatus mapStatus(String status) {
        if (status == null) return null;
        return switch (status.toLowerCase()) {
            case "ongoing" -> MediaStatus.RELEASING;
            case "completed" -> MediaStatus.FINISHED;
            case "hiatus" -> MediaStatus.HIATUS;
            case "cancelled" -> MediaStatus.CANCELLED;
            default -> null;
        };
    }
    
}

