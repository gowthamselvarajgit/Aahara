package com.aahara.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WorkoutSetResponseDto {
    private String id;
    private String sessionId;
    private String exerciseId;
    private Integer setNumber;
    private String setType;
    private BigDecimal weightKg;
    private Integer reps;
    private Integer durationSeconds;
    private BigDecimal distanceMeters;
    private BigDecimal rpe;
    private Boolean isCompleted;
    private String clientId;
    private String syncId;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
