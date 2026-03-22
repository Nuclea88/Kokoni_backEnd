package com.example.kokoni.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.example.kokoni.dto.response.MangaDetailResponse;
import com.example.kokoni.dto.response.MangaSummaryResponse;
import com.example.kokoni.service.MangaService;
@SpringBootTest
@AutoConfigureMockMvc
public class MangaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MangaService mangaService;
    @Test
    void searchMangas_ReturnsOk() throws Exception {
        // Arrange
        MangaSummaryResponse mockResponse = new MangaSummaryResponse(
            1L, "ext-123", "Berserk", "Kentaro Miura", "url.jpg", 9.5, List.of("Action", "Dark Fantasy"), false
        );
        when(mangaService.searchManga(anyString(), anyInt())).thenReturn(List.of(mockResponse));
        // Act & Assert (Público, no necesita token)
        mockMvc.perform(get("/api/mangas/search")
                .param("title", "Berserk")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Berserk"))
                .andExpect(jsonPath("$[0].author").value("Kentaro Miura"));
    }
    @Test
    @WithMockUser // Simula un usuario logueado para endpoints protegidos
    void getMangaDetails_ReturnsOk() throws Exception {
        // Arrange
        MangaDetailResponse mockDetail = new MangaDetailResponse(
            1L, "Berserk", "Kentaro Miura", "url.jpg", 9.5, 1, 1000, "RELEASING", 
            "Guts is a wanderer...", List.of("Action"), 364, 0, false
        );
        when(mangaService.getMangaDetails("ext-123")).thenReturn(mockDetail);
        // Act & Assert
        mockMvc.perform(get("/api/mangas/ext-123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Berserk"))
                .andExpect(jsonPath("$.rankPosition").value(1));
    }
}