package com.aahara.backend.controller;

import com.aahara.backend.dto.DiaryEntryRequestDto;
import com.aahara.backend.dto.DiaryEntryResponseDto;
import com.aahara.backend.entity.MealType;
import com.aahara.backend.service.DiaryEntryService;
import com.aahara.backend.service.DailySummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class DiaryControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @MockitoBean
    private DiaryEntryService diaryService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user123")
    public void createEntry_authenticated_returnsCreated() throws Exception {
        String jsonReq = "{\"entryDate\":\"2026-09-28\",\"mealType\":\"BREAKFAST\",\"foodId\":\"food1\",\"quantity\":100}";

        DiaryEntryResponseDto res = DiaryEntryResponseDto.builder().id("1").userId("user123").build();
        when(diaryService.createEntry(eq("user123"), any())).thenReturn(res);

        mockMvc.perform(post("/api/diary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReq))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user123"));
    }
    
    @Test
    public void createEntry_unauthenticated_returns401() throws Exception {
        String jsonReq = "{\"entryDate\":\"2026-09-28\",\"mealType\":\"BREAKFAST\",\"foodId\":\"food1\",\"quantity\":100}";
        mockMvc.perform(post("/api/diary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReq))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user123")
    public void updateEntry_enforcesOwnership() throws Exception {
        String jsonReq = "{\"entryDate\":\"2026-09-28\",\"mealType\":\"LUNCH\",\"foodId\":\"food1\",\"quantity\":200}";
        
        when(diaryService.updateEntry(eq("user123"), eq("1"), any()))
            .thenReturn(DiaryEntryResponseDto.builder().id("1").userId("user123").build());

        mockMvc.perform(put("/api/diary/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReq))
                .andExpect(status().isOk());
    }
}
