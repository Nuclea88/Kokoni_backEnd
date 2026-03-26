package com.example.kokoni.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kokoni.entity.UserCustomMedia;

@Repository
public interface UserCustomMediaRepository extends JpaRepository<UserCustomMedia, Long> {
   
    List<UserCustomMedia> findByCreatorId(Long userId);
    
    Optional<UserCustomMedia> findByCreatorIdAndBaseMangaId(Long userId, Long baseMangaId);
}