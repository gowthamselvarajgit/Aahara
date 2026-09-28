package com.aahara.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "diary_entries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLRestriction("deleted_at IS NULL")
public class DiaryEntry {
    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Column(name = "food_id", nullable = false)
    private String foodId;

    @Column(name = "portion_id")
    private String portionId;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(name = "food_name_snapshot")
    private String foodNameSnapshot;

    @Column(name = "portion_name_snapshot")
    private String portionNameSnapshot;

    @Column(name = "calories_snapshot")
    private BigDecimal caloriesSnapshot;

    @Column(name = "protein_snapshot")
    private BigDecimal proteinSnapshot;

    @Column(name = "carbs_snapshot")
    private BigDecimal carbsSnapshot;

    @Column(name = "fat_snapshot")
    private BigDecimal fatSnapshot;

    @Column(name = "fiber_snapshot")
    private BigDecimal fiberSnapshot;

    @Column(name = "nutrients_snapshot_json", columnDefinition = "json")
    private String nutrientsSnapshotJson;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "sync_id")
    private String syncId;

    @Version
    private Integer version;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
