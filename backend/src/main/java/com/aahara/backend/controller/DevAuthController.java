package com.aahara.backend.controller;

import com.aahara.backend.dto.TokenResponseDto;
import com.aahara.backend.entity.User;
import com.aahara.backend.repository.UserRepository;
import com.aahara.backend.security.CustomUserDetails;
import com.aahara.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/dev/auth")
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class DevAuthController {
    
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Value("${app.dev.allowed-email:test@aahara.local}")
    private String allowedDevEmail;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestParam String email) {
        // Prevent arbitrary impersonation
        if (!email.equals(allowedDevEmail) && !email.endsWith("@test.com")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .authProvider("DEV")
                .authProviderId(email)
                .passwordHash("dev_dummy_hash")
                .build();
            return userRepository.save(newUser);
        });
        
        CustomUserDetails userDetails = CustomUserDetails.create(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
        String token = tokenProvider.generateToken(auth);
        
        return ResponseEntity.ok(TokenResponseDto.builder().token(token).type("Bearer").build());
    }
}
