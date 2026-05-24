package com.example.kokoni.repository;

import java.time.LocalDateTime;
import java.util.Optional;
//REVISAR
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.kokoni.entity.Manga;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {
    Optional<Manga> findByExternalId(String externalId);

    @Query("SELECT DISTINCT m FROM Manga m JOIN m.titles t WHERE LOWER(t.title) = LOWER(:title)")
    Optional<Manga> findByTitleIgnoreCase(@Param("title") String title);

    @Modifying
    @Query("UPDATE Manga m SET m.hasNewUpdate = false WHERE m.hasNewUpdate = true AND m.lastUpdateAt < :threshold")
    void resetNewUpdateFlags(@Param("threshold") LocalDateTime threshold);
}