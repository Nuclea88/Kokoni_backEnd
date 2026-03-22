package com.example.kokoni.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.kokoni.dto.response.MangaDTOResponse;
import com.example.kokoni.dto.response.MangaDetailResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.utils.MediaTitleHelper;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MediaTitleHelper.class})
public interface MangaMapper {

    MangaDTOResponse toDTO(Manga manga);

    @Mapping(target = "title", source = "manga") 
    @Mapping(target = "isAddedToLibrary", source = "isAdded")
    MangaSummaryResponse toSummaryResponse(Manga manga, Boolean isAdded);
    
    @Mapping(target = "title", source = "manga") 
    @Mapping(target = "rankPosition", source = "manga.popularityCount") 
    MangaDetailResponse toDetailResponse(Manga manga, Boolean isAddedInTracker, Integer currentChapter);

}

    

