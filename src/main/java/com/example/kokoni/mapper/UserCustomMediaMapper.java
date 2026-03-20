package com.example.kokoni.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.example.kokoni.dto.request.UserCustomMediaDTORequest;
import com.example.kokoni.dto.response.UserCustomMediaDTOResponse;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.utils.MediaTitleHelper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,  uses = {MediaTitleHelper.class})
public interface UserCustomMediaMapper {

    UserCustomMediaDTOResponse toResponse(UserCustomMedia custom);

    UserCustomMedia toEntity(UserCustomMediaDTORequest request);

    @Mapping(target = "baseManga.id", source = "baseMangaId")
    void updateEntity(UserCustomMediaDTORequest request, @MappingTarget UserCustomMedia entity);
}