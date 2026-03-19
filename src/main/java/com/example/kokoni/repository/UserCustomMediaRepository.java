package com.example.kokoni.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kokoni.entity.UserCustomMedia;

@Repository
public interface UserCustomMediaRepository extends JpaRepository<UserCustomMedia, Long> {
    // Buscar todas las personalizaciones de un usuario
    List<UserCustomMedia> findByCreatorId(Long userId);
    
    // Buscar si un usuario ya tiene una versión personalizada de un manga concreto
    Optional<UserCustomMedia> findByCreatorIdAndBaseMangaId(Long userId, Long baseMangaId);
}