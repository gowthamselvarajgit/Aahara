package com.aahara.backend.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class NutritionProgressServiceTest {

    private final NutritionProgressService service = new NutritionProgressService();

    @Test
    void testTargetCompletion_Partial() {
        // 82 / 120 = 68.3333%
        BigDecimal current = new BigDecimal("82");
        BigDecimal target = new BigDecimal("120");
        BigDecimal percentage = service.calculateTargetCompletion(current, target);
        assertNotNull(percentage);
        assertEquals(new BigDecimal("68.3300"), percentage.setScale(4));
    }

    @Test
    void testTargetCompletion_Exactly100() {
        BigDecimal current = new BigDecimal("120");
        BigDecimal target = new BigDecimal("120");
        BigDecimal percentage = service.calculateTargetCompletion(current, target);
        assertNotNull(percentage);
        assertEquals(new BigDecimal("100.0000"), percentage.setScale(4));
    }

    @Test
    void testTargetCompletion_Over100() {
        BigDecimal current = new BigDecimal("150");
        BigDecimal target = new BigDecimal("120");
        BigDecimal percentage = service.calculateTargetCompletion(current, target);
        assertNotNull(percentage);
        assertEquals(new BigDecimal("125.0000"), percentage.setScale(4));
    }

    @Test
    void testTargetCompletion_ZeroCurrent() {
        BigDecimal current = BigDecimal.ZERO;
        BigDecimal target = new BigDecimal("120");
        BigDecimal percentage = service.calculateTargetCompletion(current, target);
        assertNotNull(percentage);
        assertEquals(new BigDecimal("0.0000"), percentage.setScale(4));
    }

    @Test
    void testTargetCompletion_NullCurrent() {
        BigDecimal target = new BigDecimal("120");
        BigDecimal percentage = service.calculateTargetCompletion(null, target);
        assertNotNull(percentage);
        assertEquals(new BigDecimal("0.0000"), percentage.setScale(4));
    }

    @Test
    void testTargetCompletion_ZeroTarget() {
        BigDecimal current = new BigDecimal("82");
        BigDecimal target = BigDecimal.ZERO;
        BigDecimal percentage = service.calculateTargetCompletion(current, target);
        assertNull(percentage, "Should return null to avoid division by zero");
    }

    @Test
    void testTargetCompletion_NullTarget() {
        BigDecimal current = new BigDecimal("82");
        BigDecimal percentage = service.calculateTargetCompletion(current, null);
        assertNull(percentage, "Should return null if reference target is missing");
    }
    
    @Test
    void testTargetCompletion_DecimalNutrient() {
        // e.g., Iron: 8.4 / 17 = 49.41%
        BigDecimal current = new BigDecimal("8.4");
        BigDecimal target = new BigDecimal("17");
        BigDecimal percentage = service.calculateTargetCompletion(current, target);
        assertNotNull(percentage);
        assertEquals(new BigDecimal("49.4100"), percentage.setScale(4));
    }

    @Test
    void testMacroContribution_Standard() {
        // Logged Protein: 120g (120 * 4 = 480 kcal)
        // Total Logged Cals: 2000 kcal
        // Contribution: 480 / 2000 = 24%
        BigDecimal macroGrams = new BigDecimal("120");
        BigDecimal totalCals = new BigDecimal("2000");
        BigDecimal percentage = service.calculateMacroCalorieContribution(macroGrams, totalCals, 4);
        assertNotNull(percentage);
        assertEquals(new BigDecimal("24.0000"), percentage.setScale(4));
    }

    @Test
    void testMacroContribution_ZeroGrams() {
        BigDecimal macroGrams = BigDecimal.ZERO;
        BigDecimal totalCals = new BigDecimal("2000");
        BigDecimal percentage = service.calculateMacroCalorieContribution(macroGrams, totalCals, 4);
        assertNotNull(percentage);
        assertEquals(new BigDecimal("0.0000"), percentage.setScale(4));
    }

    @Test
    void testMacroContribution_ZeroTotalCals() {
        BigDecimal macroGrams = new BigDecimal("120");
        BigDecimal totalCals = BigDecimal.ZERO;
        BigDecimal percentage = service.calculateMacroCalorieContribution(macroGrams, totalCals, 4);
        assertNull(percentage, "Should return null if total calories are zero to prevent div/0");
    }

    @Test
    void testMacroContribution_NullTotalCals() {
        BigDecimal macroGrams = new BigDecimal("120");
        BigDecimal percentage = service.calculateMacroCalorieContribution(macroGrams, null, 4);
        assertNull(percentage, "Should return null if total calories are missing");
    }
}
