package com.example.kokoni.service;

import java.util.List;

import com.example.kokoni.entity.CustomList;
import com.example.kokoni.entity.Media;
import com.example.kokoni.entity.User;

public interface CustomListService {

    void createDefaultList(User user, String name);

    CustomList createList(CustomList list, User owner);

    List<CustomList> getListsByUser(Long userId);

    void addItemToList(Long listId, Media media);

    void removeItemToList(Long listId, Long listItemId);

    void deleteList(Long id);
}
