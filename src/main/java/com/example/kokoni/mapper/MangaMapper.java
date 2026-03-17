package com.example.kokoni.mapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.example.kokoni.dto.MangaDataDTO;
import com.example.kokoni.dto.response.MangaDexResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.MediaTitle;
import com.example.kokoni.entity.enums.MediaStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) 
public interface MangaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "provider", constant = "MANGADEX")
    @Mapping(target = "description", expression = "java(mapDescription(dto.attributes().description()))")
    @Mapping(target = "releaseDate", expression = "java(mapYear(dto.attributes().year()))")
    @Mapping(target = "status", expression = "java(mapStatus(dto.attributes().status()))")
    @Mapping(target = "totalVolumes", expression = "java(parseSafeInt(dto.attributes().lastVolume()))")
    @Mapping(target = "totalChapters", expression = "java(parseSafeInt(dto.attributes().lastChapter()))")
    Manga toEntity(MangaDataDTO dto);

    @AfterMapping
default void finalizeManga(MangaDataDTO dto, @MappingTarget Manga manga) {
    // 1. Títulos (Usando un Set para evitar duplicados)
    Set<String> processedTitles = new HashSet<>();
    
    // Mapeamos los títulos principales ("title")
    if (dto.attributes().title() != null) {
        dto.attributes().title().forEach((lang, value) -> {
            if (value != null && !value.isBlank()) {
                MediaTitle mt = new MediaTitle();
                mt.setTitle(value);
                mt.setLanguageCode(lang);
                mt.setIsPrimary(lang.equals("en")); // Marcamos inglés como primario
                
                manga.addTitle(mt);
                processedTitles.add(value.toLowerCase().trim());
            }
        });
    }

    // Mapeamos los títulos alternativos ("altTitles")
    if (dto.attributes().altTitles() != null) {
        for (Map<String, String> altMap : dto.attributes().altTitles()) {
            altMap.forEach((lang, value) -> {
                // Filtramos por idiomas que nos interesan
                if (List.of("es", "en", "ja", "ja-ro").contains(lang)) {
                    String cleanValue = value.toLowerCase().trim();
                    
                    // SOLO si no lo hemos procesado antes (en 'title' o en otro 'altTitle')
                    if (!processedTitles.contains(cleanValue)) {
                        MediaTitle mt = new MediaTitle();
                        mt.setTitle(value);
                        mt.setLanguageCode(lang);
                        mt.setIsPrimary(false);
                        
                        manga.addTitle(mt);
                        processedTitles.add(cleanValue);
                    }
                }
            });
        }
    }

    // 2. Official URL de los links
    if (dto.attributes().links() != null) {
        // Priorizamos el link 'raw' (original) y si no el 'engtl' (oficial inglés)
        String url = dto.attributes().links().raw() != null ? 
                     dto.attributes().links().raw() : 
                     dto.attributes().links().engtl();
        manga.setOfficialUrl(url);
    }

    // 3. Relaciones (Imagen y Autor) - Esto se queda igual que antes
    dto.relationships().forEach(rel -> {
        if ("cover_art".equals(rel.type()) && rel.attributes() != null) {
            String fileName = (String) rel.attributes().get("fileName");
            manga.setImageUrl("https://uploads.mangadex.org/covers/" + dto.id() + "/" + fileName + ".256.jpg");
        }
        if ("author".equals(rel.type()) && rel.attributes() != null) {
            manga.setAuthor((String) rel.attributes().get("name"));
        }
    });
}

    // Método helper para evitar errores si el String no es un número válido
default Integer parseSafeInt(String value) {
    if (value == null || value.isBlank() || value.equalsIgnoreCase("null")) {
        return null;
    }
    try {
        // Quitamos posibles espacios y convertimos
        return Integer.parseInt(value.trim());
    } catch (NumberFormatException e) {
        return null; // O 0, según prefieras
    }
}
    
    default String mapDescription(Map<String, String> desc) {
        if (desc == null) return "Sin descripción";
        return desc.getOrDefault("es", desc.getOrDefault("en", "Sin descripción"));
    }

    default LocalDate mapYear(Integer year) {
        return (year != null) ? LocalDate.of(year, 1, 1) : null;
    }

    default MediaStatus mapStatus(String status) {
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