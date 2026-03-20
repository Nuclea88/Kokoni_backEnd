package com.example.kokoni.service;

import java.util.List;

import com.example.kokoni.dto.request.CustomListRequest;
import com.example.kokoni.dto.response.CustomListDetailsResponse;
import com.example.kokoni.dto.response.CustomListSummaryResponse;
import com.example.kokoni.entity.User;

public interface CustomListService {

    void createDefaultList(User user, String name);

    CustomListSummaryResponse createList(CustomListRequest request);

    List<CustomListSummaryResponse> getMyLists();

    CustomListDetailsResponse getListDetails(Long listId);

    void addMangaToList(Long listId, String externalId);

    void removeMangaFromList(Long listId, Long listItemId);

    void deleteList(Long id);
}
