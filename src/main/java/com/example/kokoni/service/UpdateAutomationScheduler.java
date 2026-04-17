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
    /**
     * Tarea 1: Limpia logs no vinculados que tengan más de 7 días.
     * Se ejecuta una vez al día (a las 3 AM).
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void autoCleanLogs() {
        log.info("Iniciando limpieza automática de logs de actualizaciones...");
        logService.cleanOldUnlinkedLogs(7);
        log.info("Limpieza de logs completada.");
    }
    /**
     * Tarea 2: Quita el flag 'hasNewUpdate' a las series tras 48 horas.
     * Se ejecuta cada hora para mantener la lista fresca.
     */
    @Scheduled(fixedRate = 3600000) // Cada hora
    @Transactional
    public void autoResetNewFlags() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(48);
        log.info("Reseteando flags de 'Novedad' anteriores a: {}", threshold);
        mangaRepository.resetNewUpdateFlags(threshold);
    }
}