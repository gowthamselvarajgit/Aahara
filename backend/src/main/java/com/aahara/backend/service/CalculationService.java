package com.aahara.backend.service;

import com.aahara.backend.dto.TargetCalculationDto;
import com.aahara.backend.entity.UserProfile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CalculationService {

    public TargetCalculationDto calculateTargets(UserProfile profile) {
        if (profile.getCurrentWeightKg() == null || profile.getHeightCm() == null || profile.getDateOfBirth() == null) {
            return null;
        }
        
        int age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
        double weight = profile.getCurrentWeightKg().doubleValue();
        double height = profile.getHeightCm().doubleValue();
        
        // Mifflin-St Jeor Equation
        double bmr = (10 * weight) + (6.25 * height) - (5 * age);
        if (profile.getSex() == UserProfile.Sex.MALE) {
            bmr += 5;
        } else if (profile.getSex() == UserProfile.Sex.FEMALE) {
            bmr -= 161;
        } else {
            bmr -= 78; // average approximation for unspecified
        }
        
        double multiplier = switch (profile.getActivityLevel()) {
            case SEDENTARY -> 1.2;
            case LIGHTLY_ACTIVE -> 1.375;
            case MODERATELY_ACTIVE -> 1.55;
            case VERY_ACTIVE -> 1.725;
            case EXTRA_ACTIVE -> 1.9;
            default -> 1.2;
        };
        
        double tdee = bmr * multiplier;
        double targetCalories = tdee;
        
        if (profile.getGoal() == UserProfile.Goal.LOSE_WEIGHT) {
            targetCalories -= 500;
            // Floor to 1200 minimum for safety
            if (targetCalories < 1200) targetCalories = 1200;
        } else if (profile.getGoal() == UserProfile.Goal.GAIN_WEIGHT) {
            targetCalories += 500;
        }
        
        // Protein: 1.0g per kg of body weight (approximate ICMR/NIN active baseline)
        int targetProtein = (int) Math.round(weight * 1.0);
        // Ensure protein doesn't exceed 35% of total calories as a safety cap
        int maxProtein = (int) ((targetCalories * 0.35) / 4.0);
        targetProtein = Math.min(targetProtein, maxProtein);

        // Fat: 30% of total calories
        int targetFat = (int) Math.round((targetCalories * 0.30) / 9.0);
        
        // Carbs: Remainder of calories
        int remainingCaloriesForCarbs = (int) targetCalories - (targetProtein * 4) - (targetFat * 9);
        int targetCarbs = (int) Math.round(remainingCaloriesForCarbs / 4.0);
        if (targetCarbs < 0) targetCarbs = 0;
        
        // Water: 35ml per kg baseline estimate
        int waterMl = (int) Math.round(weight * 35);
        
        return TargetCalculationDto.builder()
                .targetCalories((int) targetCalories)
                .targetProteinG(targetProtein)
                .targetCarbsG(targetCarbs)
                .targetFatG(targetFat)
                .targetWaterMl(waterMl)
                .build();
    }
}
