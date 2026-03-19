package com.example.kokoni.service;

import org.springframework.stereotype.Service;

import com.example.kokoni.dto.request.UserCustomMediaDTORequest;
import com.example.kokoni.dto.response.UserCustomMediaDTOResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.MediaTitle;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.exception.TitleException;
import com.example.kokoni.mapper.UserCustomMediaMapper;
import com.example.kokoni.repository.UserCustomMediaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserCustomMediaServiceImpl implements UserCustomMediaService{
private final UserCustomMediaRepository repository;
private final MangaService mangaService;
private final UserCustomMediaMapper customMediaMapper;
private final UserService userService;

    @Transactional
    public UserCustomMedia createCustom(UserCustomMediaDTORequest request, Long userId) {
       
        UserCustomMedia custom = customMediaMapper.toEntity(request);
        
        User creator = userService.findById(userId);
        custom.setCreator(creator);
        custom.setProvider("USER_CUSTOM");

        if (request.baseMangaId() != null) {
            Manga base = mangaService.findById(request.baseMangaId());
            custom.setBaseManga(base);


        custom.setImageUrl(request.imageUrl() != null ? request.imageUrl() : base.getImageUrl());
        custom.setDescription(request.description() != null ? request.description() : base.getDescription());





        // if (request.description() == null) custom.setDescription(base.getDescription());
        // if (request.imageUrl() == null) custom.setImageUrl(base.getImageUrl());
        // if ( custom.getTitles().isEmpty()) {
        for (MediaTitle baseTitle : base.getTitles()) {
            MediaTitle newTitle = new MediaTitle();
            newTitle.setTitle(baseTitle.getTitle());
            newTitle.setLanguageCode(baseTitle.getLanguageCode());
            newTitle.setIsPrimary(baseTitle.getIsPrimary());
  
            custom.addTitle(newTitle); 
            }
        // }
        }
        else {
        if (custom.getTitles() == null) {
            throw new TitleException("Debe tener al menos un título.");
        
        }
         MediaTitle newTitle = new MediaTitle();
        newTitle.setTitle(request.title());
        newTitle.setLanguageCode("es"); // O el que prefieras por defecto
        newTitle.setIsPrimary(true);
        custom.addTitle(newTitle);
         }
        
         if (custom.getTitles() != null) {
        custom.getTitles().forEach(t -> t.setMedia(custom));
        }

       UserCustomMedia saved = repository.save(custom);
     System.out.println("el request es"+ request);
     System.out.println("el cusgtom es" + custom);
    // DEBUG: Mira la consola de IntelliJ/VSCode
    System.out.println("ID SALVADO: " + saved.getId());
    System.out.println("URL SALVADA: " + saved.getImageUrl());
    System.out.println("TITULOS SIZE: " + (saved.getTitles() != null ? saved.getTitles().size() : "NULL"));
    
    return saved;
}

    @Override
    public UserCustomMedia findById(Long id){
        return (repository.findById(id))
            .orElseThrow(() -> new EntityNotFoundException("Media personalizado no encontrado"));
    }

    @Override
    public UserCustomMediaDTOResponse searchById(Long id) {
        return customMediaMapper.toResponse(findById(id));  
    }

    @Override
    @Transactional
    public UserCustomMedia update(Long id, UserCustomMediaDTORequest request) {
    UserCustomMedia existing = findById(id);
    
    customMediaMapper.updateEntity(request, existing);
    if (request.title() != null && !request.title().isBlank()) {
        existing.getTitles().stream()
            .filter(MediaTitle::getIsPrimary)
            .findFirst()
            .ifPresent(t -> t.setTitle(request.title()));
    }
    return repository.save(existing);
}

    @Override
    @Transactional
    public void delete(Long id) {
        UserCustomMedia media = findById(id);
        repository.delete(media);
    }
}

