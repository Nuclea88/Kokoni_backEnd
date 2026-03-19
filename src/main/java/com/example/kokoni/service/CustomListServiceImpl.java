package com.example.kokoni.service;

import com.example.kokoni.repository.ListItemRepository;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.kokoni.entity.CustomList;
import com.example.kokoni.entity.ListItem;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.Media;
import com.example.kokoni.entity.User;
import com.example.kokoni.repository.CustomListRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomListServiceImpl implements CustomListService{
    private final CustomListRepository customListRepository;
    private final ListItemService listItemService;

    @Override
    @Transactional
    public void createDefaultList(User user, String name) {
        CustomList list = new CustomList();
        list.setName(name);
        list.setOwner(user);
        list.setIsPublic(false); 
        customListRepository.save(list);
    }

    @Override
    @Transactional
    public CustomList createList(CustomList list, User owner) {
        list.setOwner(owner); 
        return customListRepository.save(list); 
    }

    public List<CustomList> getListsByUser(Long userId) {
        return customListRepository.findByOwnerId(userId);
    }

    public CustomList getListById(Long listId){
        return customListRepository.findById(listId)
            .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada"));
    }

    @Override
    @Transactional
    public void addItemToList(Long listId, Media media) {
        CustomList list = getListById(listId);
        listItemService.create(list, media);
    }

    @Override
    @Transactional
    public void removeItemToList (Long listId, Long listItemId){
        CustomList list = getListById(listId);
             list.getItems().removeIf(item -> item.getId().equals(listItemId));
    }

     @Override
    @Transactional
    public void deleteList(Long id) {
        if (!customListRepository.existsById(id)) {
            throw new EntityNotFoundException("No se puede eliminar: lista no encontrada");
        }
        customListRepository.deleteById(id);
    }
}
