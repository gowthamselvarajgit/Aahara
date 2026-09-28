package com.aahara.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkoutSetRequestDto {
    @NotNull(message = "Exercise ID is required")
    private String exerciseId;
    
    @NotNull(message = "Set number is required")
    @PositiveOrZero(message = "Set number cannot be negative")
    private Integer setNumber;
    
    private String setType;
    
    @PositiveOrZero
    private BigDecimal weightKg;
    
    @PositiveOrZero
    private Integer reps;
    
    @PositiveOrZero
    private Integer durationSeconds;
    
    @PositiveOrZero
    private BigDecimal distanceMeters;
    
    @PositiveOrZero
    private BigDecimal rpe;
    
    private Boolean isCompleted;
    private String clientId;
}
