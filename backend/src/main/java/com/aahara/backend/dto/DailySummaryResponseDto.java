package com.aahara.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class DailySummaryResponseDto {
    private String userId;
    private LocalDate date;
    
    private NutritionSummary calories;
    private NutritionSummary protein;
    private NutritionSummary carbohydrates;
    private NutritionSummary fat;
    private NutritionSummary fiber;
    private NutritionSummary water;
    
    @Data
    @Builder
    public static class NutritionSummary {
        private BigDecimal current;
        private BigDecimal target;
        private BigDecimal completionPercentage;
    }
}
