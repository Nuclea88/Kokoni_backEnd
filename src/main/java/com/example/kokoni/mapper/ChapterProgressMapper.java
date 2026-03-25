package com.example.kokoni.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.kokoni.dto.request.AddChapterProgressRequest;
import com.example.kokoni.dto.response.ChapterProgressResponse;
import com.example.kokoni.entity.UserChapterProgress;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChapterProgressMapper {
    
    ChapterProgressResponse toResponse(UserChapterProgress progress);

    @Mapping(target = "readDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "isSpecial", defaultValue = "false")
    UserChapterProgress toEntity(AddChapterProgressRequest request);
}