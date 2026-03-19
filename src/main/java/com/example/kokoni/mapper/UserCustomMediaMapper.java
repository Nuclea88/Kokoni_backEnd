package com.example.kokoni.mapper;

import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.kokoni.dto.request.UserCustomMediaDTORequest;
import com.example.kokoni.dto.response.UserCustomMediaDTOResponse;
import com.example.kokoni.entity.MediaTitle;
import com.example.kokoni.entity.UserCustomMedia;

@Mapper(componentModel = "spring")
public interface UserCustomMediaMapper {

    @Mapping(target = "title", expression = "java(custom.getTitles() != null && !custom.getTitles().isEmpty() ? custom.getTitles().get(0).getTitle() : \"Sin título\")")
    UserCustomMediaDTOResponse toResponse(UserCustomMedia custom);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "titles", ignore = true) 
    @Mapping(target = "creator", ignore = true)
    UserCustomMedia toEntity(UserCustomMediaDTORequest request);

    default String getPrimaryTitle(UserCustomMedia custom) {
        if (custom == null || custom.getTitles() == null || custom.getTitles().isEmpty()) return "Sin título";
        return custom.getTitles().stream()
            .filter(t -> t != null && Boolean.TRUE.equals(t.getIsPrimary()))
                .map(MediaTitle::getTitle)
                .findFirst()
                .orElse(custom.getTitles().get(0).getTitle());

    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "baseManga.id", source = "baseMangaId")
    void updateEntity(UserCustomMediaDTORequest request, @MappingTarget UserCustomMedia entity);
}