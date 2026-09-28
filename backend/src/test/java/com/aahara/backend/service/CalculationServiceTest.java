package com.aahara.backend.service;

import com.aahara.backend.dto.TargetCalculationDto;
import com.aahara.backend.entity.UserProfile;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CalculationServiceTest {

    @Test
    void testTargetCalculation_LoseWeight() {
        CalculationService service = new CalculationService();
        
        UserProfile profile = UserProfile.builder()
                .currentWeightKg(new java.math.BigDecimal("80.0"))
                .heightCm(new java.math.BigDecimal("180.0"))
                .dateOfBirth(LocalDate.now().minusYears(30))
                .sex(UserProfile.Sex.MALE)
                .activityLevel(UserProfile.ActivityLevel.SEDENTARY)
                .goal(UserProfile.Goal.LOSE_WEIGHT)
                .build();
                
        TargetCalculationDto targets = service.calculateTargets(profile);
        
        assertNotNull(targets);
        // BMR = 1780, TDEE = 2136. Lose weight = 2136 - 500 = 1636
        assertEquals(1636, targets.getTargetCalories());
        
        // Protein = 80 * 1.0 = 80g
        assertEquals(80, targets.getTargetProteinG());
        
        // Fat = (1636 * 0.3) / 9 = 55g
        assertEquals(55, targets.getTargetFatG());
        
        // Carbs = (1636 - (80*4) - (55*9)) / 4 = (1636 - 320 - 495) / 4 = 821 / 4 = 205g
        assertEquals(205, targets.getTargetCarbsG());
        
        assertEquals(2800, targets.getTargetWaterMl());
    }
}
