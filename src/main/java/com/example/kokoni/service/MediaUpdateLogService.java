package com.example.kokoni.service;

import com.example.kokoni.entity.MediaUpdateLog;
import java.util.List;

public interface MediaUpdateLogService {

    void saveLog(String originalTitle, String chapter, String source, Long mediaId);

    List<MediaUpdateLog> getPendingUpdates();
    
    void cleanOldUnlinkedLogs(int days);
}