package com.example.kokoni.service;

import org.springframework.stereotype.Service;

import com.example.kokoni.entity.CustomList;
import com.example.kokoni.entity.ListItem;
import com.example.kokoni.entity.Media;
import com.example.kokoni.repository.ListItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListItemServiceImpl implements ListItemService {
    private final ListItemRepository listItemRepository;

    @Override
    @Transactional
    public ListItem create(CustomList list, Media media) {

        ListItem item = new ListItem();
        item.setList(list);
        item.setMedia(media);
        return listItemRepository.save(item);
    }

    @Override
    public void delete(Long listItemId) {
        listItemRepository.deleteById(listItemId); ;
    }
}
