package com.example.kokoni.mapper;

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
    @Mapping(target = "description", source = "attributes.description", qualifiedByName = "mapDescription")
    @Mapping(target = "releaseDate", source = "attributes.year")
    @Mapping(target = "status", source = "attributes.status")
    @Mapping(target = "author", source = "relationships", qualifiedByName = "extractAuthor")
    @Mapping(target = "imageUrl", source = ".", qualifiedByName = "extractCoverUrl")
    @Mapping(target = "totalVolumes", source = "attributes.lastVolume", qualifiedByName = "parseSafeInt")
    @Mapping(target = "totalChapters", source = "attributes.lastChapter", qualifiedByName = "parseSafeInt")
    Manga toEntity(MangaDexDataDTO dto);

    @AfterMapping
    default void finalizeManga(MangaDexDataDTO dto, @MappingTarget Manga manga) {
    
        mapTitles(dto, manga);
        mapLinks(dto, manga);
    }

     default void mapTitles(MangaDexDataDTO dto, Manga manga) {
        Set<String> processedTitles = new HashSet<>();
        
        if (dto.attributes().title() != null) {
            dto.attributes().title().forEach((lang, value) -> {
                if (value != null && !value.isBlank()) {
                    manga.addTitle(createMediaTitle(value, lang, lang.equals("en")));
                    processedTitles.add(value.toLowerCase().trim());
                }
            });
        }

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
}