package com.aahara.backend.dto;
import lombok.*;

@Data @Builder
public class TargetCalculationDto {
    private Integer targetCalories;
    private Integer targetProteinG;
    private Integer targetCarbsG;
    private Integer targetFatG;
    private Integer targetWaterMl;
}
