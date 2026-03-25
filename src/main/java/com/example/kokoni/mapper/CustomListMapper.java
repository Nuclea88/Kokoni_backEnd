package com.example.kokoni.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.example.kokoni.dto.response.CustomListDetailsResponse;
import com.example.kokoni.dto.response.CustomListSummaryResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.entity.CustomList;
import com.example.kokoni.entity.ListItem;
import com.example.kokoni.utils.MediaMetadataHelper;
import com.example.kokoni.utils.MediaTitleHelper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MangaMapper.class, MediaTitleHelper.class, MediaMetadataHelper.class})
public interface CustomListMapper {

    @Mapping(target = "itemCount", expression = "java(list.getItems() != null ? list.getItems().size() : 0)")
    CustomListSummaryResponse toSummaryResponse(CustomList list);

    CustomListDetailsResponse toDetailResponse(CustomList list);
    
    @Mapping(target = "id", source = "media.id")
    @Mapping(target = "title", source = "media", qualifiedByName = "extractTitle")
    @Mapping(target = "imageUrl", source = "media.imageUrl")
    @Mapping(target = "isAddedToLibrary", constant = "true")
    @Mapping(target = "externalId", source = "media", qualifiedByName = "mapExternalId") 
    MangaSummaryResponse mapListItemToManga(ListItem item);
    
}