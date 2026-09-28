package com.aahara.backend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "foods")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Food {
    @Id
    private String id;
    private String name;
    private String tamilName;
    private String searchAliases;
    private String brand;
    private String category;
    @Enumerated(EnumType.STRING)
    private FoodState foodState;
    private String source;
    private String sourceUrl;
    private Boolean isVerified;
    
    public enum FoodState { RAW, COOKED, BOILED, FRIED, STEAMED, BAKED, PREPARED, PACKAGED, UNKNOWN }
}
