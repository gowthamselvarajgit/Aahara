package com.aahara.backend.dto;

import com.aahara.backend.entity.MealType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiaryEntryRequestDto {
    @NotNull(message = "Entry date is required")
    private LocalDate entryDate;
    
    @NotNull(message = "Meal type is required")
    private MealType mealType;
    
    @NotNull(message = "Food ID is required")
    private String foodId;
    
    private String portionId;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;
    
    private String clientId;
    private Integer version;
}
