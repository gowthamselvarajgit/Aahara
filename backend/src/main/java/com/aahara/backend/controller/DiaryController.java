package com.aahara.backend.controller;

import com.aahara.backend.dto.DiaryEntryRequestDto;
import com.aahara.backend.dto.DiaryEntryResponseDto;
import com.aahara.backend.dto.DailySummaryResponseDto;
import com.aahara.backend.service.DiaryEntryService;
import com.aahara.backend.service.DailySummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryEntryService diaryService;
    private final DailySummaryService summaryService;

    @PostMapping
    public ResponseEntity<DiaryEntryResponseDto> createEntry(Authentication authentication, @Valid @RequestBody DiaryEntryRequestDto request) {
        String userId = getUserId(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.createEntry(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryEntryResponseDto> updateEntry(Authentication authentication, @PathVariable String id, @Valid @RequestBody DiaryEntryRequestDto request) {
        return ResponseEntity.ok(diaryService.updateEntry(getUserId(authentication), id, request));
    }

    @GetMapping
    public ResponseEntity<Page<DiaryEntryResponseDto>> getEntries(
            Authentication authentication,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String userId = getUserId(authentication);
        if (from == null) from = LocalDate.now().minusDays(30);
        if (to == null) to = LocalDate.now();
        if (size > 100) size = 100;
        return ResponseEntity.ok(diaryService.getEntries(userId, from, to, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaryEntryResponseDto> getEntry(Authentication authentication, @PathVariable String id) {
        return ResponseEntity.ok(diaryService.getEntry(getUserId(authentication), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(Authentication authentication, @PathVariable String id) {
        diaryService.deleteEntry(getUserId(authentication), id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/summary")
    public ResponseEntity<DailySummaryResponseDto> getSummary(
            Authentication authentication, 
            @RequestParam(required = false) LocalDate date) {
        if(date == null) date = LocalDate.now();
        return ResponseEntity.ok(summaryService.getSummary(getUserId(authentication), date));
    }

    private String getUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("Authentication required");
        }
        return authentication.getName();
    }
}
