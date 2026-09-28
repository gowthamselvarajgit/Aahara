package com.aahara.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_aliases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FoodAlias {
    @Id
    private String id;
    
    @Column(name = "food_id", nullable = false)
    private String foodId;
    
    @Column(nullable = false)
    private String alias;
    
    @Column(name = "normalized_alias", nullable = false)
    private String normalizedAlias;
    
    @Column(nullable = false)
    private String language;
    
    @Column(name = "alias_type", nullable = false)
    private String aliasType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
