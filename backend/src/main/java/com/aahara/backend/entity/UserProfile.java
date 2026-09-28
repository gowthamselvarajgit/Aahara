package com.aahara.backend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile {
    @Id
    private String userId;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private java.math.BigDecimal heightCm;
    private java.math.BigDecimal currentWeightKg;
    private java.math.BigDecimal targetWeightKg;
    @Enumerated(EnumType.STRING)
    private Goal goal;
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;
    private Integer targetCalories;
    private Integer targetProteinG;
    private Integer targetCarbsG;
    private Integer targetFatG;
    private Integer targetWaterMl;
    
    public enum Sex { MALE, FEMALE, OTHER }
    public enum Goal { LOSE_WEIGHT, MAINTAIN_WEIGHT, GAIN_WEIGHT }
    public enum ActivityLevel { SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE, EXTRA_ACTIVE }
}
