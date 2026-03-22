package com.example.kokoni.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.kokoni.entity.CustomList;
import com.example.kokoni.entity.User;
import com.example.kokoni.repository.CustomListRepository;
@ExtendWith(MockitoExtension.class)
public class CustomListServiceImplTest {
    @Mock
    private CustomListRepository listRepository;
    @Mock
    private AuthService authService;
    @InjectMocks
    private CustomListServiceImpl listService;
    private User myUser;
    private CustomList myList;
    @BeforeEach
    void setUp() {
        myUser = new User();
        myUser.setId(1L);
        myList = new CustomList();
        myList.setId(10L);
        myList.setOwner(myUser);
        myList.setName("Mis Shonens Favoritos");
    }
    @Test
    void deleteList_Success() {
        // Arrange
        when(authService.getAuthenticatedUser()).thenReturn(myUser);
        when(listRepository.findById(10L)).thenReturn(Optional.of(myList));
        // Act
        // Borramos la lista "10L" (que es nuestra)
        listService.deleteList(10L);
        // Assert
        verify(listRepository, times(1)).delete(myList); // Verificamos que el repositorio ejecutó "delete"
    }
    @Test
    void deleteList_NotOwner_ThrowsException() {
        // Arrange: Simulamos que otro usuario está intentando borrar MI lista
        User anotherUser = new User();
        anotherUser.setId(99L); // ID distinto
        when(authService.getAuthenticatedUser()).thenReturn(anotherUser);
        when(listRepository.findById(10L)).thenReturn(Optional.of(myList));
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listService.deleteList(10L);
        });
        assertEquals("Acceso denegado: Esta lista no te pertenece.", exception.getMessage());
        verify(listRepository, never()).delete(any(CustomList.class)); // Debe bloquearse antes de borrar
    }
}