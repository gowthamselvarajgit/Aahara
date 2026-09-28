package com.aahara.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WaterLogRequestDto {
    @NotNull(message = "Entry date is required")
    private LocalDate entryDate;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Integer amountMl;
    
    private String clientId;
}
