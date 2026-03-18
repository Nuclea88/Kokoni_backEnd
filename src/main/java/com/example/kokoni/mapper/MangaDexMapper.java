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

import com.example.kokoni.dto.MangaDexResponse.MangaDexDataDTO;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.MediaTitle;

@Mapper(componentModel = "spring", 
unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses ={MangaDexConverter.class}) 
public interface MangaDexMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "provider", constant = "MANGADEX")
    @Mapping(target = "description", source = "attributes.description")
    @Mapping(target = "releaseDate", source = "attributes.year")
    @Mapping(target = "status", source = "attributes.status")
    @Mapping(target = "totalVolumes", source = "attributes.lastVolume")
    @Mapping(target = "totalChapters", source = "attributes.lastChapter")
    Manga toEntity(MangaDexDataDTO dto);

    @AfterMapping
default void finalizeManga(MangaDexDataDTO dto, @MappingTarget Manga manga) {
    
        mapTitles(dto, manga);
        mapLinks(dto, manga);
        mapRelationships(dto, manga);
    }
    
    private void mapTitles(MangaDexDataDTO dto, Manga manga) {
        Set<String> processedTitles = new HashSet<>();
        
        // Lógica de títulos principales
        if (dto.attributes().title() != null) {
            dto.attributes().title().forEach((lang, value) -> {
                if (value != null && !value.isBlank()) {
                    manga.addTitle(createMediaTitle(value, lang, lang.equals("en")));
                    processedTitles.add(value.toLowerCase().trim());
                }
            });
        }

        // Lógica de títulos alternativos
        if (dto.attributes().altTitles() != null) {
            for (Map<String, String> altMap : dto.attributes().altTitles()) {
                altMap.forEach((lang, value) -> {
                    if (List.of("es", "en", "ja", "ja-ro").contains(lang)) {
                        String cleanValue = value.toLowerCase().trim();
                        if (!processedTitles.contains(cleanValue)) {
                            manga.addTitle(createMediaTitle(value, lang, false));
                            processedTitles.add(cleanValue);
                        }
                    }
                });
            }
        }
    }

    private MediaTitle createMediaTitle(String title, String lang, boolean isPrimary) {
        MediaTitle mt = new MediaTitle();
        mt.setTitle(title);
        mt.setLanguageCode(lang);
        mt.setIsPrimary(isPrimary);
        return mt;
    }

    private void mapLinks(MangaDexDataDTO dto, Manga manga) {
        if (dto.attributes().links() != null) {
            String url = dto.attributes().links().raw() != null ? 
                         dto.attributes().links().raw() : 
                         dto.attributes().links().engtl();
            manga.setOfficialUrl(url);
        }
    }

    private void mapRelationships(MangaDexDataDTO dto, Manga manga) {
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
}
    
    
    
    
    
    
    
    
    
    
//     // 1. Títulos (Usando un Set para evitar duplicados)
//     Set<String> processedTitles = new HashSet<>();
    
//     // Mapeamos los títulos principales ("title")
//     if (dto.attributes().title() != null) {
//         dto.attributes().title().forEach((lang, value) -> {
//             if (value != null && !value.isBlank()) {
//                 MediaTitle mt = new MediaTitle();
//                 mt.setTitle(value);
//                 mt.setLanguageCode(lang);
//                 mt.setIsPrimary(lang.equals("en")); // Marcamos inglés como primario
                
//                 manga.addTitle(mt);
//                 processedTitles.add(value.toLowerCase().trim());
//             }
//         });
//     }

//     // Mapeamos los títulos alternativos ("altTitles")
//     if (dto.attributes().altTitles() != null) {
//         for (Map<String, String> altMap : dto.attributes().altTitles()) {
//             altMap.forEach((lang, value) -> {
//                 // Filtramos por idiomas que nos interesan
//                 if (List.of("es", "en", "ja", "ja-ro").contains(lang)) {
//                     String cleanValue = value.toLowerCase().trim();
                    
//                     // SOLO si no lo hemos procesado antes (en 'title' o en otro 'altTitle')
//                     if (!processedTitles.contains(cleanValue)) {
//                         MediaTitle mt = new MediaTitle();
//                         mt.setTitle(value);
//                         mt.setLanguageCode(lang);
//                         mt.setIsPrimary(false);
                        
//                         manga.addTitle(mt);
//                         processedTitles.add(cleanValue);
//                     }
//                 }
//             });
//         }
//     }

//     // 2. Official URL de los links
//     if (dto.attributes().links() != null) {
//         // Priorizamos el link 'raw' (original) y si no el 'engtl' (oficial inglés)
//         String url = dto.attributes().links().raw() != null ? 
//                      dto.attributes().links().raw() : 
//                      dto.attributes().links().engtl();
//         manga.setOfficialUrl(url);
//     }

//     // 3. Relaciones (Imagen y Autor) - Esto se queda igual que antes
//     dto.relationships().forEach(rel -> {
//         if ("cover_art".equals(rel.type()) && rel.attributes() != null) {
//             String fileName = (String) rel.attributes().get("fileName");
//             manga.setImageUrl("https://uploads.mangadex.org/covers/" + dto.id() + "/" + fileName + ".256.jpg");
//         }
//         if ("author".equals(rel.type()) && rel.attributes() != null) {
//             manga.setAuthor((String) rel.attributes().get("name"));
//         }
//     });
// }
// }