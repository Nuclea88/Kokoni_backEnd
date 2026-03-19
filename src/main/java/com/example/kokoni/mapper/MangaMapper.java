package com.example.kokoni.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.kokoni.dto.response.MangaDTOResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.Media;
import com.example.kokoni.entity.MediaTitle;

@Mapper(componentModel = "spring")
public interface MangaMapper {

    @Mapping(target = "title", expression = "java(getPrimaryTitle(manga))")
    MangaDTOResponse toDTO(Manga manga);

    default String getPrimaryTitle(Media media) {
        if (media.getTitles() == null || media.getTitles().isEmpty()) return null;
        return media.getTitles().stream()
                .filter(t -> Boolean.TRUE.equals(t.getIsPrimary()))
                .findFirst()
                .map(MediaTitle::getTitle)
                .orElse(media.getTitles().get(0).getTitle());
    }
}
