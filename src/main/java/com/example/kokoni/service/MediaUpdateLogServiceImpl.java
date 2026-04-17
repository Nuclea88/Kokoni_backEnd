package com.example.kokoni.service;
//REVISAR
import com.example.kokoni.entity.MediaUpdateLog;
import com.example.kokoni.repository.MediaUpdateLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaUpdateLogServiceImpl implements MediaUpdateLogService{
    private final MediaUpdateLogRepository logRepository;

    @Override
    @Transactional
public void saveLog(String originalTitle, String chapter, String source, Long mediaId) {
        MediaUpdateLog log = new MediaUpdateLog();
        log.setOriginalTitle(originalTitle);
        log.setExtractedChapter(chapter);
        log.setSource(source);
        log.setMediaId(mediaId);
        logRepository.save(log);
    }
    @Override
    public List<MediaUpdateLog> getPendingUpdates() {
        return logRepository.findByMediaIdIsNull();
    }
    @Override
    @Transactional
    public void cleanOldUnlinkedLogs(int days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        logRepository.deleteByMediaIdIsNullAndCreatedAtBefore(threshold);
    }
}