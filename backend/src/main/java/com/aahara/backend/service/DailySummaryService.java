package com.aahara.backend.service;

import com.aahara.backend.dto.DailySummaryResponseDto;
import com.aahara.backend.entity.DiaryEntry;
import com.aahara.backend.entity.UserProfile;
import com.aahara.backend.entity.WaterLog;
import com.aahara.backend.repository.DiaryEntryRepository;
import com.aahara.backend.repository.UserProfileRepository;
import com.aahara.backend.repository.WaterLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailySummaryService {
    private final DiaryEntryRepository diaryRepository;
    private final WaterLogRepository waterRepository;
    private final UserProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public DailySummaryResponseDto getSummary(String userId, LocalDate date) {
        List<DiaryEntry> entries = diaryRepository.findByUserIdAndEntryDate(userId, date);
        List<WaterLog> waters = waterRepository.findByUserIdAndEntryDate(userId, date);
        UserProfile profile = profileRepository.findById(userId).orElse(null);

        BigDecimal cal = BigDecimal.ZERO;
        BigDecimal pro = BigDecimal.ZERO;
        BigDecimal carb = BigDecimal.ZERO;
        BigDecimal fat = BigDecimal.ZERO;
        BigDecimal fib = BigDecimal.ZERO;

        for (DiaryEntry e : entries) {
            if (e.getCaloriesSnapshot() != null) cal = cal.add(e.getCaloriesSnapshot());
            if (e.getProteinSnapshot() != null) pro = pro.add(e.getProteinSnapshot());
            if (e.getCarbsSnapshot() != null) carb = carb.add(e.getCarbsSnapshot());
            if (e.getFatSnapshot() != null) fat = fat.add(e.getFatSnapshot());
            if (e.getFiberSnapshot() != null) fib = fib.add(e.getFiberSnapshot());
        }

        BigDecimal wtr = BigDecimal.valueOf(waters.stream().mapToInt(WaterLog::getAmountMl).sum());

        return DailySummaryResponseDto.builder()
            .userId(userId)
            .date(date)
            .calories(buildSummary(cal, profile != null && profile.getTargetCalories() != null ? BigDecimal.valueOf(profile.getTargetCalories()) : null))
            .protein(buildSummary(pro, profile != null && profile.getTargetProteinG() != null ? BigDecimal.valueOf(profile.getTargetProteinG()) : null))
            .carbohydrates(buildSummary(carb, profile != null && profile.getTargetCarbsG() != null ? BigDecimal.valueOf(profile.getTargetCarbsG()) : null))
            .fat(buildSummary(fat, profile != null && profile.getTargetFatG() != null ? BigDecimal.valueOf(profile.getTargetFatG()) : null))
            .fiber(buildSummary(fib, null))
            .water(buildSummary(wtr, profile != null && profile.getTargetWaterMl() != null ? BigDecimal.valueOf(profile.getTargetWaterMl()) : null))
            .build();
    }

    private DailySummaryResponseDto.NutritionSummary buildSummary(BigDecimal current, BigDecimal target) {
        BigDecimal pct = null;
        if (target != null && target.compareTo(BigDecimal.ZERO) > 0) {
            pct = current.multiply(new BigDecimal("100")).divide(target, 1, RoundingMode.HALF_UP);
        }
        return DailySummaryResponseDto.NutritionSummary.builder()
            .current(current)
            .target(target)
            .completionPercentage(pct)
            .build();
    }
}
