package com.example.kokoni.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.kokoni.dto.request.RegisterRequest;
import com.example.kokoni.entity.User;
import com.example.kokoni.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService; // Mockeamos el servicio para no tocar la BD real
    @Test
    void register_ValidRequest_ReturnsCreated() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("newUser", "new@mail.com", "pass123", null);
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("newUser");
        when(userService.registerUser(any(RegisterRequest.class))).thenReturn(mockUser);
        // Act & Assert
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuario registrado con éxito. Ya puedes iniciar sesión.")); 
    }
    @Test
    void register_InvalidEmail_ReturnsBadRequest() throws Exception {
        // Arrange: Email sin formato válido
        RegisterRequest request = new RegisterRequest("newUser", "correo-invalido", "pass123", null);
        // Act & Assert: El @Valid del controlador debe frenar la petición y dar un 400
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}