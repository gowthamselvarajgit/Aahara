package com.aahara.backend.dto;

import com.aahara.backend.entity.MealType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class DiaryEntryResponseDto {
    private String id;
    private String userId;
    private LocalDate entryDate;
    private MealType mealType;
    private String foodId;
    private String portionId;
    private BigDecimal quantity;
    
    private String foodNameSnapshot;
    private String portionNameSnapshot;
    private BigDecimal caloriesSnapshot;
    private BigDecimal proteinSnapshot;
    private BigDecimal carbsSnapshot;
    private BigDecimal fatSnapshot;
    private BigDecimal fiberSnapshot;
    private String nutrientsSnapshotJson;
    
    private String clientId;
    private String syncId;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
