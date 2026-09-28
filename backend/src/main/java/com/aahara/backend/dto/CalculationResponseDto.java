package com.aahara.backend.dto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
@Data @Builder
public class CalculationResponseDto {
    private String methodologyVersion;
    private NutrientDto calories;
    private NutrientDto protein;
    private NutrientDto carbohydrates;
    private NutrientDto fat;
    private NutrientDto water;
    
    @Data @Builder
    public static class NutrientDto {
        private BigDecimal value;
        private String unit;
    }
}
