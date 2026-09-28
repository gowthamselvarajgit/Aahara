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
    
    private String ownerUserId;
    private String datasetVersion;
    private String sourceRecordId;
    private String license;
    @Enumerated(EnumType.STRING)
    private FoodStatus status;
    
    public enum FoodStatus { ACTIVE, DEPRECATED, LEGAL_REVIEW_REQUIRED, USER_CREATED, INTERNAL_CURATED }
    
    public enum FoodState { RAW, COOKED, BOILED, FRIED, STEAMED, BAKED, PREPARED, PACKAGED, UNKNOWN }

}
