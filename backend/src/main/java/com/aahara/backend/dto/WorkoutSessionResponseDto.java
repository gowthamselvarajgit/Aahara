package com.aahara.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WorkoutSessionResponseDto {
    private String id;
    private String userId;
    private String routineId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
    private String clientId;
    private String syncId;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
