package com.example.kokoni.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.kokoni.dto.response.MangaDetailResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.User;
import com.example.kokoni.mapper.ChapterProgressMapper;
import com.example.kokoni.mapper.MangaMapper;
import com.example.kokoni.repository.MangaRepository;
import com.example.kokoni.repository.UserChapterProgressRepository;
import com.example.kokoni.repository.UserCustomMediaRepository;
import com.example.kokoni.repository.UserMediaTrackerRepository;

@ExtendWith(MockitoExtension.class)
public class MangaServiceImplTest {

    @Mock private MangaProvider mangaProvider;
    @Mock private MangaRepository mangaRepository;
    @Mock private MangaMapper mangaMapper;
    @Mock private AuthService authService;
    @Mock private UserMediaTrackerRepository trackerRepository;
    @Mock private UserCustomMediaRepository customMediaRepository;
    @Mock private UserChapterProgressRepository progressRepository;
    @Mock private ChapterProgressMapper chapterProgressMapper;

    @InjectMocks
    private MangaServiceImpl mangaService;

    private Manga mockManga;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockManga = new Manga();
        mockManga.setId(100L);
        mockManga.setExternalId("ext-123");

        mockUser = new User();
        mockUser.setId(1L);
    }

    @Test
    void searchManga_ReturnsList() {
        // Arrange
        when(mangaProvider.searchManga("Test", 1)).thenReturn(List.of(mockManga));
        when(authService.getOptionalAuthenticatedUser()).thenReturn(mockUser);
        when(trackerRepository.existsByUserIdAndMediaExternalId(1L, "ext-123")).thenReturn(true);
        MangaSummaryResponse mockResponse = new MangaSummaryResponse(100L, "ext-123", "Test Manga", null, null, null, null, true);
        when(mangaMapper.toSummaryResponse(mockManga, true)).thenReturn(mockResponse);

        // Act
        List<MangaSummaryResponse> result = mangaService.searchManga("Test", 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Manga", result.get(0).title());
        assertTrue(result.get(0).isAddedToLibrary());
    }

    @Test
    void getMangaDetails_ExistingManga_ReturnsDetails() {
        // Arrange
        when(mangaRepository.findByExternalId("ext-123")).thenReturn(Optional.of(mockManga));
        when(authService.getOptionalAuthenticatedUser()).thenReturn(mockUser);
        when(customMediaRepository.findByCreatorIdAndBaseMangaId(1L, 100L)).thenReturn(Optional.empty());
        when(trackerRepository.findByUserIdAndMediaId(1L, 100L)).thenReturn(Optional.empty());
        
        MangaDetailResponse mockResponse = new MangaDetailResponse(
            1L, "Test Manga", null, null, null, null, null, null, null, null, null, null, false, null, null, List.of()
        );
        when(mangaMapper.toDetailResponse(eq(mockManga), eq(false), eq(0), eq(null), anyList(), eq(null))).thenReturn(mockResponse);

        // Act
        MangaDetailResponse result = mangaService.getMangaDetails("ext-123");

        // Assert
        assertNotNull(result);
        assertEquals("Test Manga", result.title());
    }
}
