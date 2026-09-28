package com.aahara.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkoutSessionRequestDto {
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String routineId;
    private String notes;
    private String clientId;
    private Integer version;
}
