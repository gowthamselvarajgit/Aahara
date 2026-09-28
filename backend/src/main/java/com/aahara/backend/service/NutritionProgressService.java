package com.aahara.backend.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NutritionProgressService {

    private static final int SCALE = 4; // API returns precise percentages, UI formats to whole numbers.
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * Calculates the completion percentage of a specific target.
     * Examples: Calories, Protein, Water, Iron.
     * 
     * @param current The current logged amount.
     * @param target The target or reference amount.
     * @return The percentage (e.g., 75.50), or null if the target is 0 or null.
     */
    public BigDecimal calculateTargetCompletion(BigDecimal current, BigDecimal target) {
        if (target == null || target.compareTo(BigDecimal.ZERO) == 0) {
            return null; // Prevent division by zero or invention of percentages
        }
        if (current == null) {
            current = BigDecimal.ZERO;
        }
        
        return current.divide(target, SCALE, RoundingMode.HALF_UP).multiply(ONE_HUNDRED);
    }

    /**
     * Calculates the caloric contribution percentage of a specific macronutrient 
     * against the total logged calories.
     * 
     * @param macroGrams The logged amount of the macronutrient in grams.
     * @param totalCalories The total calories logged.
     * @param kcalPerGram The Atwater factor (4 for Protein/Carbs, 9 for Fat).
     * @return The percentage of total calories contributed, or null if totalCalories is <= 0.
     */
    public BigDecimal calculateMacroCalorieContribution(BigDecimal macroGrams, BigDecimal totalCalories, int kcalPerGram) {
        if (totalCalories == null || totalCalories.compareTo(BigDecimal.ZERO) <= 0) {
            return null; // Prevent division by zero or negative calorie bases
        }
        if (macroGrams == null) {
            macroGrams = BigDecimal.ZERO;
        }
        
        BigDecimal macroCalories = macroGrams.multiply(new BigDecimal(kcalPerGram));
        return macroCalories.divide(totalCalories, SCALE, RoundingMode.HALF_UP).multiply(ONE_HUNDRED);
    }
}
