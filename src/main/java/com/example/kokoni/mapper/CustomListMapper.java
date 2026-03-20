package com.example.kokoni.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.example.kokoni.dto.response.CustomListDetailsResponse;
import com.example.kokoni.dto.response.CustomListSummaryResponse;
import com.example.kokoni.entity.CustomList;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MangaMapper.class})
public interface CustomListMapper {

    @Mapping(target = "itemCount", expression = "java(list.getItems() != null ? list.getItems().size() : 0)")
    CustomListSummaryResponse toSummaryResponse(CustomList list);

    CustomListDetailsResponse toDetailResponse(CustomList list);
    
    default com.example.kokoni.dto.response.MangaSummaryResponse mapListItemToManga(com.example.kokoni.entity.ListItem item, MangaMapper mangaMapper) {
        return mangaMapper.toSummaryResponse((com.example.kokoni.entity.Manga) item.getMedia(), true);
    }
}