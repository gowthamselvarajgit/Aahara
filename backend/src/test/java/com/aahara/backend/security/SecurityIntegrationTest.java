package com.aahara.backend.security;

import com.aahara.backend.entity.User;
import com.aahara.backend.repository.UserRepository;
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
public class SecurityIntegrationTest {

    
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String validToken;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        userRepository.deleteAll();
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email("test@example.com")
                .passwordHash("dummy")
                .build();
        userRepository.save(user);

        CustomUserDetails userDetails = CustomUserDetails.create(user);
        validToken = jwtTokenProvider.generateToken(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

    @Test
    void unauthenticatedRequestToProtectedEndpoint_Returns401() throws Exception {
        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }

    @Test
    void authenticatedRequestToProtectedEndpoint_Returns200() throws Exception {
        mockMvc.perform(get("/api/foods").header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }

    @Test
    void malformedJwt_Returns401() throws Exception {
        mockMvc.perform(get("/api/foods").header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }
    
    @Test
    void publicEndpointBehavior_IsIntentional() throws Exception {
        mockMvc.perform(get("/api/public/ping"))
                .andExpect(status().isOk()); 
    }
}
