package com.aahara.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "food_portions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FoodPortion {
    @Id
    private String id;
    @Column(name = "food_id", nullable = false)
    private String foodId;
    @Column(nullable = false)
    private String description;
    @Column(name = "gram_weight", nullable = false)
    private BigDecimal gramWeight;
}
