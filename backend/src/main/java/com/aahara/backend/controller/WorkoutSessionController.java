package com.aahara.backend.controller;

import com.aahara.backend.dto.WorkoutSessionRequestDto;
import com.aahara.backend.dto.WorkoutSessionResponseDto;
import com.aahara.backend.service.WorkoutSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutSessionController {
    private final WorkoutSessionService sessionService;

    @PostMapping
    public ResponseEntity<WorkoutSessionResponseDto> createSession(Authentication authentication, @Valid @RequestBody WorkoutSessionRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.createSession(getUserId(authentication), request));
    }

    @GetMapping
    public ResponseEntity<Page<WorkoutSessionResponseDto>> getSessions(
            Authentication authentication,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (from == null) from = LocalDateTime.now().minusDays(30);
        if (to == null) to = LocalDateTime.now();
        if (size > 100) size = 100;
        return ResponseEntity.ok(sessionService.getSessions(getUserId(authentication), from, to, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSessionResponseDto> getSession(Authentication authentication, @PathVariable String id) {
        return ResponseEntity.ok(sessionService.getSession(getUserId(authentication), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutSessionResponseDto> updateSession(Authentication authentication, @PathVariable String id, @Valid @RequestBody WorkoutSessionRequestDto request) {
        return ResponseEntity.ok(sessionService.updateSession(getUserId(authentication), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(Authentication authentication, @PathVariable String id) {
        sessionService.deleteSession(getUserId(authentication), id);
        return ResponseEntity.noContent().build();
    }

    private String getUserId(Authentication authentication) {
        return authentication.getName();
    }
}
