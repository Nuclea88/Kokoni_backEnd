package com.example.kokoni.service;

import com.example.kokoni.repository.ListItemRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.kokoni.dto.request.CustomListRequest;
import com.example.kokoni.dto.response.CustomListDetailsResponse;
import com.example.kokoni.dto.response.CustomListSummaryResponse;
import com.example.kokoni.entity.CustomList;
import com.example.kokoni.entity.ListItem;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.Media;
import com.example.kokoni.entity.User;
import com.example.kokoni.mapper.CustomListMapper;
import com.example.kokoni.repository.CustomListRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomListServiceImpl implements CustomListService{

    private final CustomListRepository listRepository;
    private final ListItemRepository itemRepository;
    private final AuthService authService;
    private final MangaService mangaService;
    private final CustomListMapper listMapper;
    private final UserCustomMediaService customMediaService;

    private CustomList getMyListOrThrow(Long listId) {
        User me = authService.getAuthenticatedUser();
        CustomList list = listRepository.findById(listId)
            .orElseThrow(() -> new EntityNotFoundException("Lista no encontrada"));
            
        if (!list.getOwner().getId().equals(me.getId())) {
            throw new RuntimeException("Acceso denegado: Esta lista no te pertenece.");
        }
        return list;
    }

    @Override
    @Transactional
    public void createDefaultList(User user, String name) {
        CustomList list = new CustomList();
        list.setName(name);
        list.setOwner(user);
        list.setIsPublic(false); 
        listRepository.save(list);
    }

    @Override
    @Transactional
    public CustomListSummaryResponse createList(CustomListRequest request) {
        User me = authService.getAuthenticatedUser(); 
        
        CustomList list = new CustomList();
        list.setName(request.name());
        list.setOwner(me);
        list.setIsPublic(request.isPublic() != null ? request.isPublic() : false);
        return listMapper.toSummaryResponse(listRepository.save(list));
    }

    @Override
    public List<CustomListSummaryResponse> getMyLists() {
      User me = authService.getAuthenticatedUser();
      List<CustomListSummaryResponse> myLists = listRepository.findByOwnerId(me.getId()).stream()
                                                .map(listMapper::toSummaryResponse)
                                                .collect(Collectors.toList());
        return myLists;
    }

    @Override
    public CustomListDetailsResponse getListDetails(Long listId) {
        return listMapper.toDetailResponse(getMyListOrThrow(listId));
    }

    @Override
    @Transactional
    public void addMangaToList(Long listId, String externalId) {
        CustomList list = getMyListOrThrow(listId);
        
        Media media;
        if (externalId.matches("\\d+")) {
            media = customMediaService.findById(Long.parseLong(externalId));
        } else {
            media = mangaService.searchAndSave(externalId);
        }
        if (itemRepository.existsByListIdAndMediaId(listId, media.getId())) {
             throw new RuntimeException("Este manga ya está en tu lista.");
        }
        ListItem item = new ListItem();
        item.setList(list);
        item.setMedia(media);
        itemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeMangaFromList(Long listId, Long listItemId) {
        CustomList list = getMyListOrThrow(listId);
        
        ListItem item = itemRepository.findById(listItemId)
            .orElseThrow(() -> new EntityNotFoundException("Elemento no encontrado en la lista"));
            
        if (!item.getList().getId().equals(list.getId())) {
            throw new RuntimeException("Ese item no pertenece a esta lista.");
        }
        
        itemRepository.delete(item);
    }

     @Override
    @Transactional
    public void deleteList(Long listId) {
        CustomList list = getMyListOrThrow(listId);
        listRepository.delete(list);
    }

    @Override
    @Transactional
    public void removeFromAllMyLists(String id) {
        User me = authService.getAuthenticatedUser();
        List<ListItem> itemsToDelete = itemRepository.findAll().stream()
            .filter(item -> item.getList().getOwner().getId().equals(me.getId()))
            .filter(item ->{
                 if (id.matches("\\d+")) {
                return item.getMedia().getId().equals(Long.parseLong(id));
                } else {
                    return id.equals(item.getMedia().getExternalId());
                }
            })
            .collect(java.util.stream.Collectors.toList());
        
        itemRepository.deleteAll(itemsToDelete);
    }
}
