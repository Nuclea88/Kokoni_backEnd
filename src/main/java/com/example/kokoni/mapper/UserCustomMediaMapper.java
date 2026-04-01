package com.example.kokoni.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.example.kokoni.dto.request.UserCustomMediaDTORequest;
import com.example.kokoni.dto.response.ChapterProgressResponse;
import com.example.kokoni.dto.response.UserCustomMediaDTOResponse;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.utils.MediaTitleHelper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MediaTitleHelper.class },nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE 
)
public interface UserCustomMediaMapper {

    @Mapping(target = "title", source = "custom", qualifiedByName = "extractTitle")
    UserCustomMediaDTOResponse toResponse(UserCustomMedia custom);

    @Mapping(target = "title", source = "custom", qualifiedByName = "extractTitle")
    @Mapping(target = "isAddedInTracker", source = "isAddedInTracker")
    @Mapping(target = "trackerId", source = "trackerId")
    @Mapping(target = "readChapters", source = "readChapters")
    @Mapping(target = "currentChapter", source = "currentChapter")
    UserCustomMediaDTOResponse toResponse(UserCustomMedia custom, Boolean isAddedInTracker, Long trackerId, List<ChapterProgressResponse> readChapters, Integer currentChapter, String userStatus);

    UserCustomMedia toEntity(UserCustomMediaDTORequest request);

    // @Mapping(target = "baseManga.id", source = "baseMangaId")
    void updateEntity(UserCustomMediaDTORequest request, @MappingTarget UserCustomMedia entity);
}