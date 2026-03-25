package com.example.kokoni.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.kokoni.dto.request.RegisterRequest;
import com.example.kokoni.entity.User;
import com.example.kokoni.mapper.UserMapper;
import com.example.kokoni.repository.UserChapterProgressRepository;
import com.example.kokoni.repository.UserRepository;
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomListService customListService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AuthService authService;
    @Mock
    private UserChapterProgressRepository progressRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User testUser;
    private RegisterRequest registerRequest;
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testPlayer");
        testUser.setEmail("test@kokoni.com");
        testUser.setPassword("encodedPassword");
        registerRequest = new RegisterRequest("testPlayer", "test@kokoni.com", "password123", null);
    }
    @Test
    void loadUserByUsername_Success() {
        // Arrange
        when(userRepository.findByUsername("testPlayer")).thenReturn(Optional.of(testUser));
        // Act
        UserDetails userDetails = userService.loadUserByUsername("testPlayer");
        // Assert
        assertNotNull(userDetails);
        assertEquals("testPlayer", userDetails.getUsername());
    }
    @Test
    void registerUser_Success() {
        // Arrange
        when(userRepository.existsByUsername(registerRequest.username())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(userMapper.toEntity(registerRequest)).thenReturn(testUser);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        // Act
        User result = userService.registerUser(registerRequest);
        // Assert
        assertNotNull(result);
        assertEquals("testPlayer", result.getUsername());
        verify(customListService, times(1)).createDefaultList(testUser, "Favoritos");
    }
    @Test
    void registerUser_UsernameAlreadyExists_ThrowsException() {
        // Arrange
        when(userRepository.existsByUsername(registerRequest.username())).thenReturn(true);
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(registerRequest);
        });
        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
        // Verificamos que NUNCA intentó guardarlo
        verify(userRepository, never()).save(any(User.class)); 
    }
}