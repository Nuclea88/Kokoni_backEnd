package com.example.kokoni.service;

import org.springframework.stereotype.Service;

import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.MediaTitle;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.exception.TitleException;
import com.example.kokoni.repository.UserCustomMediaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserCustomMediaServiceImpl implements UserCustomMediaService{
private final UserCustomMediaRepository repository;
private final MangaService mangaService;

    @Transactional
    public UserCustomMedia createCustom(UserCustomMedia custom, User creator) {
        custom.setCreator(creator);

        if (custom.getBaseManga() != null) {
        Manga base = mangaService.findById(custom.getBaseManga().getId());
        
        if (custom.getDescription() == null) custom.setDescription(base.getDescription());
        if (custom.getImageUrl() == null) custom.setImageUrl(base.getImageUrl());
        if (custom.getTitles() == null || custom.getTitles().isEmpty()) {
        for (MediaTitle baseTitle : base.getTitles()) {
            MediaTitle newTitle = new MediaTitle();
            newTitle.setTitle(baseTitle.getTitle());
            newTitle.setLanguageCode(baseTitle.getLanguageCode());
            newTitle.setIsPrimary(baseTitle.getIsPrimary());
  
            custom.addTitle(newTitle); 
            }
        }
        }
        
        // if (custom.getTitles() == null || custom.getTitles().isEmpty()) {
        //     throw new TitleException("Debe tener al menos un título.");
        
        // }
         if (custom.getTitles() != null) {
        custom.getTitles().forEach(t -> t.setMedia(custom));
         }
        custom.setProvider("USER_CUSTOM");
        return repository.save(custom);
    }

    public UserCustomMedia findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Media personalizado no encontrado"));
    }
}

