package com.example.kokoni.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.kokoni.entity.UserMediaTracker;

@Repository
public interface UserMediaTrackerRepository extends JpaRepository<UserMediaTracker, Long> {
    
    List<UserMediaTracker> findByUserId(Long userId);
    
    Optional<UserMediaTracker> findByUserIdAndMediaId(Long userId, Long mediaId);
    
    boolean existsByUserIdAndMediaId(Long userId, Long mediaId);

    boolean existsByUserIdAndMediaExternalId(Long userId, String externalId);
}