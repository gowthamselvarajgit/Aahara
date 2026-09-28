package com.aahara.backend.controller;
import com.aahara.backend.dto.CalculationRequestDto;
import com.aahara.backend.dto.CalculationResponseDto;
import com.aahara.backend.dto.TargetCalculationDto;
import com.aahara.backend.entity.UserProfile;
import com.aahara.backend.service.CalculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/calculations")
@RequiredArgsConstructor
public class CalculationController {
    
    private final CalculationService calculationService;

    @PostMapping("/targets")
    public ResponseEntity<CalculationResponseDto> calculateTargets(@Valid @RequestBody CalculationRequestDto request) {
        UserProfile profile = UserProfile.builder()
                .dateOfBirth(request.getDateOfBirth())
                .sex(UserProfile.Sex.valueOf(request.getSex().toUpperCase()))
                .heightCm(request.getHeightCm())
                .currentWeightKg(request.getCurrentWeightKg())
                .goal(UserProfile.Goal.valueOf(request.getGoal().toUpperCase()))
                .activityLevel(UserProfile.ActivityLevel.valueOf(request.getActivityLevel().toUpperCase()))
                .build();
                
        TargetCalculationDto targets = calculationService.calculateTargets(profile);
        
        CalculationResponseDto response = CalculationResponseDto.builder()
                .methodologyVersion("v1.5-mifflin-icmr1.0")
                .calories(CalculationResponseDto.NutrientDto.builder().value(new BigDecimal(targets.getTargetCalories())).unit("kcal").build())
                .protein(CalculationResponseDto.NutrientDto.builder().value(new BigDecimal(targets.getTargetProteinG())).unit("g").build())
                .carbohydrates(CalculationResponseDto.NutrientDto.builder().value(new BigDecimal(targets.getTargetCarbsG())).unit("g").build())
                .fat(CalculationResponseDto.NutrientDto.builder().value(new BigDecimal(targets.getTargetFatG())).unit("g").build())
                .water(CalculationResponseDto.NutrientDto.builder().value(new BigDecimal(targets.getTargetWaterMl())).unit("ml").build())
                .build();
                
        return ResponseEntity.ok(response);
    }
}
