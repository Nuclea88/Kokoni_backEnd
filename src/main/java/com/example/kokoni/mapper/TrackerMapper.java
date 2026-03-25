package com.example.kokoni.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.example.kokoni.dto.response.TrackerItemResponse;
import com.example.kokoni.entity.UserMediaTracker;
import com.example.kokoni.utils.MediaMetadataHelper;
import com.example.kokoni.utils.MediaTitleHelper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MediaTitleHelper.class, MediaMetadataHelper.class })
public interface TrackerMapper {

    @Mapping(target = "trackerId", source = "id")
    @Mapping(target = "mangaId", source = "media.id")
    @Mapping(target = "mangaImageUrl", source = "media.imageUrl")
    @Mapping(target = "mangaTitle", source = "media", qualifiedByName = "extractTitle")
    @Mapping(target = "userStatus", source = "userStatus")
    @Mapping(target = "progressUnit", source = "tracker")
    @Mapping(target = "totalChapters", source = "media", qualifiedByName = "calculateTotalChapters")
    @Mapping(target = "externalId", source = "media", qualifiedByName = "mapExternalId") 
     TrackerItemResponse toTrackerItemResponse(UserMediaTracker tracker);

}