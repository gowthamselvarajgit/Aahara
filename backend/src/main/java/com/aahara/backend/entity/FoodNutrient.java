package com.aahara.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "food_nutrients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FoodNutrient {
    @Id
    private String id;
    @Column(name = "food_id", nullable = false)
    private String foodId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "nutrient_type", nullable = false)
    private NutrientType nutrientType;
    
    @Column(name = "amount_per_100g", nullable = false)
    private BigDecimal amountPer100g;
}
