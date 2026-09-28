package com.aahara.backend.service;

import com.aahara.backend.dto.DiaryEntryRequestDto;
import com.aahara.backend.dto.DiaryEntryResponseDto;
import com.aahara.backend.entity.DiaryEntry;
import com.aahara.backend.entity.Food;
import com.aahara.backend.entity.FoodNutrient;
import com.aahara.backend.entity.FoodPortion;
import com.aahara.backend.repository.DiaryEntryRepository;
import com.aahara.backend.repository.FoodNutrientRepository;
import com.aahara.backend.repository.FoodPortionRepository;
import com.aahara.backend.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryEntryService {
    private final DiaryEntryRepository diaryRepository;
    private final FoodRepository foodRepository;
    private final FoodPortionRepository portionRepository;
    private final FoodNutrientRepository nutrientRepository;

    @Transactional
    public DiaryEntryResponseDto createEntry(String userId, DiaryEntryRequestDto request) {
        // Validation & Loading
        Food food = foodRepository.findById(request.getFoodId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid food ID"));
            
        FoodPortion portion = null;
        if (request.getPortionId() != null) {
            portion = portionRepository.findById(request.getPortionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid portion ID"));
            if (!portion.getFoodId().equals(food.getId())) {
                throw new IllegalArgumentException("Portion does not belong to food");
            }
        }

        // Calculate Nutrition on Server
        BigDecimal factor;
        if (portion != null) {
            factor = request.getQuantity().multiply(portion.getGramWeight()).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        } else {
            factor = request.getQuantity().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        }

        List<FoodNutrient> nutrients = nutrientRepository.findByFoodId(food.getId());
        
        DiaryEntry entry = DiaryEntry.builder()
            .id(UUID.randomUUID().toString())
            .userId(userId)
            .entryDate(request.getEntryDate())
            .mealType(request.getMealType())
            .foodId(food.getId())
            .portionId(portion != null ? portion.getId() : null)
            .quantity(request.getQuantity())
            .foodNameSnapshot(food.getName())
            .portionNameSnapshot(portion != null ? portion.getDescription() : "100g")
            .clientId(request.getClientId())
            .build();

        for (FoodNutrient n : nutrients) {
            BigDecimal val = n.getAmountPer100g().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            switch (n.getNutrientType().name()) {
                case "CALORIES": entry.setCaloriesSnapshot(val); break;
                case "PROTEIN": entry.setProteinSnapshot(val); break;
                case "CARBOHYDRATE": entry.setCarbsSnapshot(val); break;
                case "FAT": entry.setFatSnapshot(val); break;
                case "FIBER": entry.setFiberSnapshot(val); break;
            }
        }
        
        // Use JSON for extensive nutrients
        entry.setNutrientsSnapshotJson("{}"); // Simplified for now

        DiaryEntry saved = diaryRepository.save(entry);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<DiaryEntryResponseDto> getEntries(String userId, LocalDate from, LocalDate to, Pageable pageable) {
        return diaryRepository.findByUserIdAndEntryDateBetweenOrderByEntryDateDesc(userId, from, to, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public DiaryEntryResponseDto getEntry(String userId, String id) {
        DiaryEntry entry = diaryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Diary entry not found or unauthorized"));
        return mapToDto(entry);
    }

    @Transactional
    public void deleteEntry(String userId, String id) {
        DiaryEntry entry = diaryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Diary entry not found or unauthorized"));
        entry.setDeletedAt(LocalDateTime.now());
        diaryRepository.save(entry); // Soft delete
    }

    private DiaryEntryResponseDto mapToDto(DiaryEntry entity) {
        return DiaryEntryResponseDto.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .entryDate(entity.getEntryDate())
            .mealType(entity.getMealType())
            .foodId(entity.getFoodId())
            .portionId(entity.getPortionId())
            .quantity(entity.getQuantity())
            .foodNameSnapshot(entity.getFoodNameSnapshot())
            .portionNameSnapshot(entity.getPortionNameSnapshot())
            .caloriesSnapshot(entity.getCaloriesSnapshot())
            .proteinSnapshot(entity.getProteinSnapshot())
            .carbsSnapshot(entity.getCarbsSnapshot())
            .fatSnapshot(entity.getFatSnapshot())
            .fiberSnapshot(entity.getFiberSnapshot())
            .nutrientsSnapshotJson(entity.getNutrientsSnapshotJson())
            .clientId(entity.getClientId())
            .syncId(entity.getSyncId())
            .version(entity.getVersion())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
