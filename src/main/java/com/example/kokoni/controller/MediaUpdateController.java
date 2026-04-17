package com.example.kokoni.controller;
//REVISAR
import com.example.kokoni.entity.MediaUpdateLog;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.MediaTitle;
import com.example.kokoni.repository.MangaRepository;
import com.example.kokoni.repository.MediaUpdateLogRepository;
import com.example.kokoni.service.MediaUpdateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/admin/updates")
@RequiredArgsConstructor
public class MediaUpdateController {
    private final MediaUpdateLogService logService;
    private final MediaUpdateLogRepository logRepository; 
    private final MangaRepository mangaRepository;
    // 1. Endpoint para listar todos los logs "huérfanos" (Los que el bot no entendió)
    @GetMapping("/pending")
    public ResponseEntity<List<MediaUpdateLog>> getPendingUpdates() {
        return ResponseEntity.ok(logService.getPendingUpdates());
    }
    // 2. Endpoint para vincular manualmente y hacer que el sistema APRENDA
    @PostMapping("/link/{logId}")
    @Transactional
    public ResponseEntity<String> linkUpdate(@PathVariable Long logId, @RequestParam Long mangaId) {
        
        Optional<MediaUpdateLog> logOpt = logRepository.findById(logId);
        Optional<Manga> mangaOpt = mangaRepository.findById(mangaId);
        if (logOpt.isEmpty() || mangaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Log o Manga no encontrado");
        }
        MediaUpdateLog updateLog = logOpt.get();
        Manga manga = mangaOpt.get();
        // 1. Vinculamos el log para que desaparezca de la lista de huérfanos
        updateLog.setMediaId(manga.getId());
        logRepository.save(updateLog);
        // 2. Actualizamos los datos de la obra (El flag de "Novedad")
        manga.setHasNewUpdate(true);
        manga.setLastUpdateAt(LocalDateTime.now());
        manga.setUpdateSource(updateLog.getSource());
        try {
            int newCap = Integer.parseInt(updateLog.getExtractedChapter().split("\\s+|-")[0]);
            if (manga.getTotalChapters() == null || newCap > manga.getTotalChapters()) {
                manga.setTotalChapters(newCap);
            }
        } catch (Exception ignored) {}
        // 3. ¡LA MAGIA!: Añadimos este título raro como un título oficial de la obra
        // Así, la próxima vez que el bot lo lea en el grupo privado, lo reconocerá solo.
        boolean titleExists = manga.getTitles().stream()
                .anyMatch(t -> t.getTitle().equalsIgnoreCase(updateLog.getOriginalTitle()));
        
        if (!titleExists) {
            MediaTitle newTitle = new MediaTitle();
            newTitle.setTitle(updateLog.getOriginalTitle());
            newTitle.setLanguageCode("es"); // Lo marcamos como ES por defecto
            newTitle.setIsPrimary(false);
            newTitle.setMedia(manga);
            manga.getTitles().add(newTitle);
        }
        mangaRepository.save(manga);
        return ResponseEntity.ok("Vinculado correctamente. El sistema ha aprendido el nuevo título.");
    }
}