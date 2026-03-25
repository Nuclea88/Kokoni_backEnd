package com.example.kokoni.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.kokoni.dto.request.AddTrackerRequest;
import com.example.kokoni.dto.response.TrackerItemResponse;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.User;
import com.example.kokoni.entity.UserMediaTracker;
import com.example.kokoni.entity.enums.UserStatus;
import com.example.kokoni.mapper.TrackerMapper;
import com.example.kokoni.repository.UserMediaTrackerRepository;
@ExtendWith(MockitoExtension.class)
public class UserMediaTrackerServiceImplTest {
    @Mock
    private UserMediaTrackerRepository trackerRepository;
    @Mock
    private MangaService mangaService;
    @Mock
    private AuthService authService;
    @Mock
    private TrackerMapper trackerMapper;
    @InjectMocks
    private UserMediaTrackerServiceImpl trackerService;
    private User testUser;
    private Manga testManga;
    private AddTrackerRequest addRequest;
    private UserMediaTracker expectedTracker;
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(10L);
        testManga = new Manga();
        testManga.setId(100L);
        testManga.setExternalId("ext-manga-123");
        addRequest = new AddTrackerRequest("ext-manga-123", UserStatus.PLANNING, null);
        expectedTracker = new UserMediaTracker();
        expectedTracker.setId(500L);
        expectedTracker.setUser(testUser);
        expectedTracker.setMedia(testManga);
    }
    @Test
    void addTracker_Success() {
        // Arrange
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        when(mangaService.searchAndSave(anyString())).thenReturn(testManga);
        // Simulamos que NO existe en la BD aún
        when(trackerRepository.existsByUserIdAndMediaId(testUser.getId(), testManga.getId())).thenReturn(false);
        when(trackerRepository.save(any(UserMediaTracker.class))).thenReturn(expectedTracker);
        
        TrackerItemResponse mockResponse = new TrackerItemResponse(500L, 100L, "Berserk", "url.jpg", "RELEASING", "PLANNING", 0, 364, 0.0, null);
        when(trackerMapper.toTrackerItemResponse(expectedTracker)).thenReturn(mockResponse);
        // Act
        TrackerItemResponse result = trackerService.addTracker(addRequest);
        // Assert
        assertNotNull(result);
        assertEquals(500L, result.trackerId());
        verify(trackerRepository, times(1)).save(any(UserMediaTracker.class));
    }
    @Test
    void addTracker_AlreadyExists_ThrowsException() {
        // Arrange
        when(authService.getAuthenticatedUser()).thenReturn(testUser);
        when(mangaService.searchAndSave(anyString())).thenReturn(testManga);
        // Simulamos que el usuario YA lo tiene en su lista
        when(trackerRepository.existsByUserIdAndMediaId(testUser.getId(), testManga.getId())).thenReturn(true);
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackerService.addTracker(addRequest);
        });
        
        assertEquals("Este manga ya está en tu lista. Usa update para modificarlo.", exception.getMessage());
        verify(trackerRepository, never()).save(any(UserMediaTracker.class));
    }
}