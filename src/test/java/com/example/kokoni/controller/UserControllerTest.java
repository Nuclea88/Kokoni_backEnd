package com.example.kokoni.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.kokoni.dto.request.UpdateUserRequest;
import com.example.kokoni.dto.response.UserProfileResponse;
import com.example.kokoni.entity.User;
import com.example.kokoni.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser
    void getUserById_ReturnsOk() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        when(userService.findById(anyLong())).thenReturn(mockUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @WithMockUser
    void getMyProfile_ReturnsOk() throws Exception {
        UserProfileResponse mockProfile = new UserProfileResponse(
            1L, "testUser", "avatar.png", 10, "KAGE", 100, 5, 20
        );
        when(userService.getUserProfile()).thenReturn(mockProfile);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.rankName").value("KAGE"));
    }

    @Test
    @WithMockUser
    void updateUser_ReturnsOk() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("newUsername", "new@mail.com", null, null);
        doNothing().when(userService).updateUser(any(UpdateUserRequest.class));

        mockMvc.perform(put("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteUser_ReturnsNoContent() throws Exception {
        doNothing().when(userService).deleteUser();

        mockMvc.perform(delete("/api/users/me"))
                .andExpect(status().isNoContent());
    }
}
