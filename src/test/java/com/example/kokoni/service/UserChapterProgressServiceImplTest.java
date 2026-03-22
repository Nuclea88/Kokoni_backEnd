package com.example.kokoni.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.kokoni.dto.request.AddChapterProgressRequest;
import com.example.kokoni.dto.response.ChapterProgressResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.UserMediaTracker;
import com.example.kokoni.entity.UserChapterProgress;
import com.example.kokoni.mapper.ChapterProgressMapper; // <-- CAMBIO: Mapper correcto
import com.example.kokoni.repository.UserChapterProgressRepository;
@ExtendWith(MockitoExtension.class)
public class UserChapterProgressServiceImplTest {
    @Mock
    private UserChapterProgressRepository progressRepository;
    @Mock
    private UserMediaTrackerService trackerService;
    @Mock
    private ChapterProgressMapper progressMapper; // <-- CAMBIO: Instancia correcta
    @InjectMocks
    private UserChapterProgressServiceImpl chapterService;
    private UserMediaTracker mockTracker;
    private Manga mockManga;
    private AddChapterProgressRequest addRequest;
    @BeforeEach
    void setUp() {
        mockManga = new Manga();
        mockManga.setId(100L);
        mockManga.setTotalChapters(50); 
        mockTracker = new UserMediaTracker();
        mockTracker.setId(500L);
        mockTracker.setMedia(mockManga);
        addRequest = new AddChapterProgressRequest(10, null, false, "Me ha encantado");
    }
    @Test
    void markChapterAsRead_Success() { // <-- CAMBIO: Nombre del test actualizado
        // Arrange
        when(trackerService.getTrackerEntityInternal(500L)).thenReturn(mockTracker);
        // CAMBIO: Usar el método real del repositorio
        when(progressRepository.existsByTrackerIdAndProgressUnit(500L, 10)).thenReturn(false);
        
        UserChapterProgress progressEntity = new UserChapterProgress();
        progressEntity.setProgressUnit(10);
        when(progressMapper.toEntity(addRequest)).thenReturn(progressEntity);
        
        UserChapterProgress savedProgress = new UserChapterProgress();
        savedProgress.setId(1L);
        savedProgress.setProgressUnit(10);
        when(progressRepository.save(any(UserChapterProgress.class))).thenReturn(savedProgress);
        // CAMBIO: El record ChapterProgressResponse solo tiene 6 argumentos
        ChapterProgressResponse expectedResponse = new ChapterProgressResponse(1L, 10, null, false, null, "Me ha encantado");
        when(progressMapper.toResponse(savedProgress)).thenReturn(expectedResponse);
        // Act - CAMBIO: Nombre del método real
        ChapterProgressResponse result = chapterService.markChapterAsRead(500L, addRequest);
        // Assert
        assertNotNull(result);
        assertEquals(10, result.progressUnit());
        verify(progressRepository, times(1)).save(any(UserChapterProgress.class));
    }
    @Test
    void markChapterAsRead_AlreadyRead_ThrowsException() { // <-- CAMBIO
        // Arrange
        when(trackerService.getTrackerEntityInternal(500L)).thenReturn(mockTracker);
        when(progressRepository.existsByTrackerIdAndProgressUnit(500L, 10)).thenReturn(true);
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            chapterService.markChapterAsRead(500L, addRequest);
        });
        assertTrue(exception.getMessage().contains("Ese capítulo ya está marcado como leído"));
        verify(progressRepository, never()).save(any(UserChapterProgress.class));
    }
}