package com.aahara.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "workout_sets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLRestriction("deleted_at IS NULL")
public class WorkoutSet {
    @Id
    private String id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "exercise_id", nullable = false)
    private String exerciseId;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "set_type")
    private String setType;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    private Integer reps;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "distance_meters")
    private BigDecimal distanceMeters;

    private BigDecimal rpe;

    @Column(name = "is_completed")
    private Boolean isCompleted;

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
        if(isCompleted == null) isCompleted = true;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
