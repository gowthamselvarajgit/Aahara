package com.aahara.backend.service;

import com.aahara.backend.dto.WaterLogRequestDto;
import com.aahara.backend.dto.WaterLogResponseDto;
import com.aahara.backend.entity.WaterLog;
import com.aahara.backend.repository.WaterLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaterLogService {
    private final WaterLogRepository waterLogRepository;

    @Transactional
    public WaterLogResponseDto createLog(String userId, WaterLogRequestDto request) {
        WaterLog log = WaterLog.builder()
            .id(UUID.randomUUID().toString())
            .userId(userId)
            .entryDate(request.getEntryDate())
            .amountMl(request.getAmountMl())
            .clientId(request.getClientId())
            .build();
        return mapToDto(waterLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public List<WaterLogResponseDto> getLogsForDate(String userId, LocalDate date) {
        return waterLogRepository.findByUserIdAndEntryDate(userId, date).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLog(String userId, String id) {
        WaterLog log = waterLogRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Water log not found or unauthorized"));
        log.setDeletedAt(LocalDateTime.now());
        waterLogRepository.save(log);
    }

    private WaterLogResponseDto mapToDto(WaterLog entity) {
        return WaterLogResponseDto.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .entryDate(entity.getEntryDate())
            .amountMl(entity.getAmountMl())
            .clientId(entity.getClientId())
            .syncId(entity.getSyncId())
            .version(entity.getVersion())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
