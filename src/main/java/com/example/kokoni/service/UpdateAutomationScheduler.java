package com.example.kokoni.service;
//REVISAR
import com.example.kokoni.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateAutomationScheduler {
    private final MediaUpdateLogService logService;
    private final MangaRepository mangaRepository;
    private final MediaUpdateService updateService;
    // @Scheduled(cron = "0 0 15,21 * * *")
   @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void performBiDailyTasks() {
        log.info("Despertando NeonTech: Ejecutando tareas bi-diarias (15:00 y 21:00)...");
        
        logService.cleanOldUnlinkedLogs(12);
        updateService.flushQueuedUpdates();
        
        LocalDateTime threshold = LocalDateTime.now().minusHours(48);
        mangaRepository.resetNewUpdateFlags(threshold);
        log.info("Operaciones terminadas. Database puede volver a dormir.");
    }
}