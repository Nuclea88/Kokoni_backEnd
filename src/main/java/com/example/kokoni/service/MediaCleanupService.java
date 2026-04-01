package com.example.kokoni.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.repository.ListItemRepository;
import com.example.kokoni.repository.UserCustomMediaRepository;
import com.example.kokoni.repository.UserMediaTrackerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaCleanupService {
    private final UserCustomMediaRepository customMediaRepository;
    private final UserMediaTrackerRepository trackerRepository;
    private final ListItemRepository listItemRepository;
    
    @Scheduled(cron = "0 9 * * * ?") 
    @Transactional
    public void limpiarMangasHuerfanos() {
        System.out.println("🧹 Iniciando barrido de Mangas Custom sin dueños...");
    
        List<UserCustomMedia> allCustom = customMediaRepository.findAll();
        
        int dropped = 0;
        for (UserCustomMedia media : allCustom) {
            boolean isInUseInTracker = trackerRepository.existsByMediaId(media.getId());
            boolean isInUseInCustomList = listItemRepository.existsByMediaId(media.getId());
            
            if (!isInUseInTracker && !isInUseInCustomList) {
                customMediaRepository.delete(media);
                dropped++;
            }
        }
        
        System.out.println("✨ Barrido completado. Fichas fantasma eliminadas: " + dropped);
    }
}