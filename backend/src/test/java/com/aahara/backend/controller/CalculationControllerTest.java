package com.aahara.backend.controller;

import com.aahara.backend.entity.User;
import com.aahara.backend.repository.UserRepository;
import com.aahara.backend.security.CustomUserDetails;
import com.aahara.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class CalculationControllerTest {

    
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        userRepository.deleteAll();
        User user = User.builder().id(UUID.randomUUID().toString()).email("test3@example.com").passwordHash("dummy").build();
        userRepository.save(user);
        CustomUserDetails userDetails = CustomUserDetails.create(user);
        token = jwtTokenProvider.generateToken(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

    @Test
    void calculateTargets_ValidRequest_ReturnsCalculations() throws Exception {
        String jsonRequest = "{" +
                "\"dateOfBirth\":\"1990-01-01\"," +
                "\"sex\":\"MALE\"," +
                "\"heightCm\":180.0," +
                "\"currentWeightKg\":80.0," +
                "\"goal\":\"LOSE_WEIGHT\"," +
                "\"activityLevel\":\"SEDENTARY\"" +
                "}";

        mockMvc.perform(post("/api/calculations/targets")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.methodologyVersion").exists())
                .andExpect(jsonPath("$.calories.value").exists())
                .andExpect(jsonPath("$.calories.unit").value("kcal"))
                .andExpect(jsonPath("$.protein.value").exists())
                .andExpect(jsonPath("$.protein.unit").value("g"));
    }

    @Test
    void calculateTargets_InvalidRequest_Returns400() throws Exception {
        // Missing height
        String jsonRequest = "{" +
                "\"dateOfBirth\":\"1990-01-01\"," +
                "\"sex\":\"MALE\"," +
                "\"currentWeightKg\":80.0," +
                "\"goal\":\"LOSE_WEIGHT\"," +
                "\"activityLevel\":\"SEDENTARY\"" +
                "}";

        mockMvc.perform(post("/api/calculations/targets")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.heightCm").exists());
    }
}
