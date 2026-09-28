package com.aahara.backend.controller;

import com.aahara.backend.dto.WaterLogRequestDto;
import com.aahara.backend.dto.WaterLogResponseDto;
import com.aahara.backend.service.WaterLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/water")
@RequiredArgsConstructor
public class WaterLogController {
    private final WaterLogService waterLogService;

    @PostMapping
    public ResponseEntity<WaterLogResponseDto> createLog(Authentication authentication, @Valid @RequestBody WaterLogRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(waterLogService.createLog(getUserId(authentication), request));
    }

    @GetMapping
    public ResponseEntity<List<WaterLogResponseDto>> getLogs(
            Authentication authentication,
            @RequestParam(required = false) LocalDate date) {
        if (date == null) date = LocalDate.now();
        return ResponseEntity.ok(waterLogService.getLogsForDate(getUserId(authentication), date));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(Authentication authentication, @PathVariable String id) {
        waterLogService.deleteLog(getUserId(authentication), id);
        return ResponseEntity.noContent().build();
    }

    private String getUserId(Authentication authentication) {
        return authentication.getName();
    }
}
