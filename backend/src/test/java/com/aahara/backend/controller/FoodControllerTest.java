package com.aahara.backend.controller;

import com.aahara.backend.entity.Food;
import com.aahara.backend.entity.User;
import com.aahara.backend.repository.FoodRepository;
import com.aahara.backend.repository.UserRepository;
import com.aahara.backend.security.CustomUserDetails;
import com.aahara.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class FoodControllerTest {

    
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        foodRepository.deleteAll();
        userRepository.deleteAll();

        User user = User.builder().id(UUID.randomUUID().toString()).email("test2@example.com").passwordHash("dummy").build();
        userRepository.save(user);
        CustomUserDetails userDetails = CustomUserDetails.create(user);
        token = jwtTokenProvider.generateToken(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        Food food = Food.builder().id("food-1").name("Apple").brand("Generic").build();
        foodRepository.save(food);
    }

    @Test
    void getFoods_ReturnsPagination() throws Exception {
        mockMvc.perform(get("/api/foods").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.items[0].name").value("Apple"));
    }

    @Test
    void getFoodDetail_ValidId_ReturnsDetail() throws Exception {
        mockMvc.perform(get("/api/foods/food-1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void getFoodDetail_InvalidId_Returns404() throws Exception {
        mockMvc.perform(get("/api/foods/invalid-id").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
