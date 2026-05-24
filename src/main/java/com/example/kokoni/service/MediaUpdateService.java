package com.example.kokoni.service;

//REVISAR
import com.example.kokoni.entity.Manga;
import com.example.kokoni.repository.MangaRepository;
import com.example.kokoni.external.ScanUpdateParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MediaUpdateService {
    private final MangaRepository mangaRepository;
    private final MediaUpdateLogService logService; 
    private final ScanUpdateParser updateParser;

    private record PendingMessage(String text, String source) {}
    private final Set<PendingMessage> pendingUpdates = ConcurrentHashMap.newKeySet();

    public void queueExternalUpdate(String rawText, String source) {
        pendingUpdates.add(new PendingMessage(rawText, source));
    }
    @Transactional
    public synchronized void flushQueuedUpdates() {
        if (pendingUpdates.isEmpty()) return;
        
        Set<PendingMessage> snapshot = new HashSet<>(pendingUpdates);
        pendingUpdates.clear();
        for (PendingMessage msg : snapshot) {
           
            this.processExternalUpdate(msg.text(), msg.source());
        }
    }

    @Transactional
    public void processExternalUpdate(String rawText, String source) {
       
        ScanUpdateParser.ParsedData data = updateParser.parse(rawText);
        if ("N/A".equals(data.chapter())) {
            return; 
        }
        Optional<Manga> mangaOpt = findMangaByAnyTitle(data.title());
        Long mediaId = mangaOpt.map(Manga::getId).orElse(null);
      
        if (mangaOpt.isPresent()) {
            Manga manga = mangaOpt.get();
            manga.setHasNewUpdate(true);
            manga.setLastUpdateAt(LocalDateTime.now());
            manga.setUpdateSource(source);
            
            try {
                int newCap = Integer.parseInt(data.chapter().split("\\s+|-")[0]);
                if (manga.getTotalChapters() == null || newCap > manga.getTotalChapters()) {
                    manga.setTotalChapters(newCap);
                }
            } catch (Exception e) {
            }
            mangaRepository.save(manga);
        }
        logService.saveLog(data.title(), data.chapter(), source, mediaId);
    }
    private Optional<Manga> findMangaByAnyTitle(String title) {
        return mangaRepository.findByTitleIgnoreCase(title);
    }
}