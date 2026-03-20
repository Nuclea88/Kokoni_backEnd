package com.example.kokoni.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.kokoni.entity.UserChapterProgress;

@Repository
public interface UserChapterProgressRepository extends JpaRepository<UserChapterProgress, Long> {

       List<UserChapterProgress> findByTrackerId(Long trackerId);

       boolean existsByTrackerIdAndProgressUnit(Long trackerId, Integer progressUnit);

       void deleteByTrackerIdAndProgressUnit(Long trackerId, Integer progressUnit);

       @Query("SELECT MAX(ucp.progressUnit) FROM UserChapterProgress ucp " +
                     "WHERE ucp.tracker.user.id = :userId AND ucp.tracker.media.id = :mediaId")
       Integer findHighestChapterRead(@Param("userId") Long userId, @Param("mediaId") Long mediaId);

       long countByTrackerUserId(Long userId);

       @Query("SELECT DISTINCT ucp.readDate FROM UserChapterProgress ucp " +
       "WHERE ucp.tracker.user.id = :userId " +
       "AND ucp.readDate IS NOT NULL " +
       "ORDER BY ucp.readDate DESC")
       List<LocalDate> findDistinctReadDatesByUserId(@Param("userId") Long userId);

}
