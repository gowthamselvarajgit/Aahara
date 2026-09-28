package com.aahara.backend.controller;

import com.aahara.backend.dto.WorkoutSetRequestDto;
import com.aahara.backend.dto.WorkoutSetResponseDto;
import com.aahara.backend.service.WorkoutSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts/{sessionId}/sets")
@RequiredArgsConstructor
public class WorkoutSetController {
    private final WorkoutSetService setService;

    @PostMapping
    public ResponseEntity<WorkoutSetResponseDto> createSet(
            Authentication authentication, 
            @PathVariable String sessionId, 
            @Valid @RequestBody WorkoutSetRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(setService.createSet(getUserId(authentication), sessionId, request));
    }

    @PutMapping("/{setId}")
    public ResponseEntity<WorkoutSetResponseDto> updateSet(
            Authentication authentication,
            @PathVariable String sessionId,
            @PathVariable String setId,
            @Valid @RequestBody WorkoutSetRequestDto request) {
        return ResponseEntity.ok(setService.updateSet(getUserId(authentication), sessionId, setId, request));
    }

    @GetMapping
    public ResponseEntity<List<WorkoutSetResponseDto>> getSets(
            Authentication authentication, 
            @PathVariable String sessionId) {
        return ResponseEntity.ok(setService.getSetsForSession(getUserId(authentication), sessionId));
    }

    @DeleteMapping("/{setId}")
    public ResponseEntity<Void> deleteSet(
            Authentication authentication, 
            @PathVariable String sessionId, 
            @PathVariable String setId) {
        setService.deleteSet(getUserId(authentication), sessionId, setId);
        return ResponseEntity.noContent().build();
    }

    private String getUserId(Authentication authentication) {
        return authentication.getName();
    }
}
