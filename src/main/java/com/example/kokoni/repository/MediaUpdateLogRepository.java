package com.example.kokoni.repository;
import com.example.kokoni.entity.MediaUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface MediaUpdateLogRepository extends JpaRepository<MediaUpdateLog, Long> {
    
    
    List<MediaUpdateLog> findByMediaIdIsNull();
    
    
    void deleteByMediaIdIsNullAndCreatedAtBefore(LocalDateTime threshold);
    
    
    boolean existsByOriginalTitleAndSource(String originalTitle, String source);
}