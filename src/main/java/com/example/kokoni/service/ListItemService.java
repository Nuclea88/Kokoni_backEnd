package com.example.kokoni.service;

import com.example.kokoni.entity.CustomList;
import com.example.kokoni.entity.ListItem;
import com.example.kokoni.entity.Media;

public interface ListItemService {

    ListItem create(CustomList list, Media media);
    void delete (Long listItemId);
}
