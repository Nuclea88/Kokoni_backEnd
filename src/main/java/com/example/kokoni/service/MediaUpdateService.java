package com.example.kokoni.service;

//REVISAR, HECHO CON PRISAS
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
    private final MediaUpdateLogService logService; // Usamos la Interfaz
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
            // Este es tu método antiguo intacto. ¡Hará el volcado de golpe!
            this.processExternalUpdate(msg.text(), msg.source());
        }
    }


    @Transactional
    public void processExternalUpdate(String rawText, String source) {
        // 1. Parseamos el texto (Extraer Obra y Capítulo)
        ScanUpdateParser.ParsedData data = updateParser.parse(rawText);
        // 2. Intentamos buscar la obra en nuestra BBDD
        Optional<Manga> mangaOpt = findMangaByAnyTitle(data.title());
        
        // Obtenemos el ID si se encontró la obra
        Long mediaId = mangaOpt.map(Manga::getId).orElse(null);
        // 3. Si se encontró, actualizamos los campos de la obra
        if (mangaOpt.isPresent()) {
            Manga manga = mangaOpt.get();
            manga.setHasNewUpdate(true);
            manga.setLastUpdateAt(LocalDateTime.now());
            manga.setUpdateSource(source);
            
            try {
                // Lógica de actualización de número de capítulos
                int newCap = Integer.parseInt(data.chapter().split("\\s+|-")[0]);
                if (manga.getTotalChapters() == null || newCap > manga.getTotalChapters()) {
                    manga.setTotalChapters(newCap);
                }
            } catch (Exception e) {
                // No se pudo parsear el número de capítulo, lo ignoramos
            }
            mangaRepository.save(manga);
        }
        // 4. Guardamos el log (vinculado o pendiente) a través del servicio dedicado
        logService.saveLog(data.title(), data.chapter(), source, mediaId);
    }
    private Optional<Manga> findMangaByAnyTitle(String title) {
        return mangaRepository.findByTitleIgnoreCase(title);
    }
}