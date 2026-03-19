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
        // Aquí podrías validar: ¿Ya existe este media en esta lista?
        // (Aunque el UniqueConstraint de la BD ya te protege, 
        // hacerlo aquí te permite lanzar una excepción más bonita).

        ListItem item = new ListItem();
        item.setList(list);
        item.setMedia(media);
        // El addedDate ya se pone solo en tu entidad
        
        return listItemRepository.save(item);
    }

    @Override
    public void delete(Long listItemId) {
        listItemRepository.deleteById(listItemId); ;
    }
}
