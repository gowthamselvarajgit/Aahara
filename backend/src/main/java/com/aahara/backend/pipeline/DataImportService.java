package com.aahara.backend.pipeline;

import com.aahara.backend.entity.Food;
import com.aahara.backend.entity.FoodAlias;
import com.aahara.backend.entity.FoodNutrient;
import com.aahara.backend.entity.FoodPortion;
import com.aahara.backend.entity.NutrientType;
import com.aahara.backend.repository.FoodAliasRepository;
import com.aahara.backend.repository.FoodNutrientRepository;
import com.aahara.backend.repository.FoodPortionRepository;
import com.aahara.backend.repository.FoodRepository;
import com.aahara.backend.util.StringNormalizer;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataImportService {

    private final FoodRepository foodRepository;
    private final FoodPortionRepository portionRepository;
    private final FoodNutrientRepository nutrientRepository;
    private final FoodAliasRepository aliasRepository;

    @Data @Builder
    public static class ImportRequest {
        private String source;
        private String datasetVersion;
        private String license;
        private List<RawFoodRecord> records;
    }

    @Data @Builder
    public static class RawFoodRecord {
        private String sourceRecordId;
        private String name;
        private String tamilName;
        private String category;
        private String foodState;
        private List<String> aliases;
        private List<RawPortion> portions;
        private Map<String, BigDecimal> nutrientsPer100g;
    }

    @Data @Builder
    public static class RawPortion {
        private String description;
        private BigDecimal gramWeight;
    }

    @Data @Builder
    public static class ValidationReport {
        private int recordsRead;
        private int recordsAccepted;
        private int recordsRejected;
        private List<String> errors;
        private List<String> warnings;
    }

    @Transactional
    public ValidationReport runImportPipeline(ImportRequest request) {
        ValidationReport report = ValidationReport.builder()
                .recordsRead(request.getRecords().size())
                .errors(new ArrayList<>())
                .warnings(new ArrayList<>())
                .build();

        int accepted = 0;
        int rejected = 0;

        for (RawFoodRecord raw : request.getRecords()) {
            try {
                processRecord(request, raw, report);
                accepted++;
            } catch (Exception e) {
                rejected++;
                report.getErrors().add("Failed record [" + raw.getSourceRecordId() + "]: " + e.getMessage());
            }
        }

        report.setRecordsAccepted(accepted);
        report.setRecordsRejected(rejected);
        return report;
    }

    private void processRecord(ImportRequest request, RawFoodRecord raw, ValidationReport report) {
        // 1. Validation
        if (raw.getName() == null || raw.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Food name is required");
        }
        if (raw.getSourceRecordId() == null || raw.getSourceRecordId().trim().isEmpty()) {
            throw new IllegalArgumentException("Source record ID is required for idempotent import");
        }

        validateNutrients(raw.getNutrientsPer100g(), raw.getSourceRecordId(), report);

        // 2. Idempotency Check
        Optional<Food> existing = foodRepository.findBySourceAndSourceRecordIdAndDatasetVersion(
                request.getSource(),
                raw.getSourceRecordId(),
                request.getDatasetVersion()
        );

        Food food = existing.orElseGet(() -> {
            Food f = new Food();
            f.setId(UUID.randomUUID().toString());
            f.setSource(request.getSource());
            f.setDatasetVersion(request.getDatasetVersion());
            f.setSourceRecordId(raw.getSourceRecordId());
            f.setLicense(request.getLicense());
            f.setStatus(Food.FoodStatus.ACTIVE);
            return f;
        });

        // 3. Map Fields
        food.setName(raw.getName().trim());
        food.setTamilName(raw.getTamilName() != null ? raw.getTamilName().trim() : null);
        food.setCategory(raw.getCategory());
        
        try {
            if (raw.getFoodState() != null) {
                food.setFoodState(Food.FoodState.valueOf(raw.getFoodState().toUpperCase()));
            } else {
                food.setFoodState(Food.FoodState.UNKNOWN);
            }
        } catch (IllegalArgumentException e) {
            food.setFoodState(Food.FoodState.UNKNOWN);
            report.getWarnings().add("Unknown food state for " + raw.getSourceRecordId() + ": " + raw.getFoodState());
        }

        foodRepository.save(food);

        // 4. Update Aliases
        if (!existing.isPresent() && raw.getAliases() != null) {
            for (String aliasStr : raw.getAliases()) {
                if (aliasStr == null || aliasStr.trim().isEmpty()) continue;
                FoodAlias alias = FoodAlias.builder()
                        .id(UUID.randomUUID().toString())
                        .foodId(food.getId())
                        .alias(aliasStr.trim())
                        .normalizedAlias(StringNormalizer.normalize(aliasStr))
                        .language("en") // Simplified
                        .aliasType("SEARCH_ALIAS")
                        .build();
                aliasRepository.save(alias);
            }
        }

        // 5. Update Portions
        if (!existing.isPresent() && raw.getPortions() != null) {
            for (RawPortion rp : raw.getPortions()) {
                if (rp.getGramWeight().compareTo(BigDecimal.ZERO) <= 0) {
                    report.getWarnings().add("Skipped negative/zero portion for " + raw.getSourceRecordId());
                    continue;
                }
                FoodPortion portion = FoodPortion.builder()
                        .id(UUID.randomUUID().toString())
                        .foodId(food.getId())
                        .description(rp.getDescription().trim())
                        .gramWeight(rp.getGramWeight())
                        .build();
                portionRepository.save(portion);
            }
        }

        // 6. Update Nutrients
        if (!existing.isPresent() && raw.getNutrientsPer100g() != null) {
            for (Map.Entry<String, BigDecimal> entry : raw.getNutrientsPer100g().entrySet()) {
                try {
                    NutrientType type = NutrientType.valueOf(entry.getKey().toUpperCase());
                    FoodNutrient fn = FoodNutrient.builder()
                            .id(UUID.randomUUID().toString())
                            .foodId(food.getId())
                            .nutrientType(type)
                            .amountPer100g(entry.getValue())
                            .build();
                    nutrientRepository.save(fn);
                } catch (IllegalArgumentException e) {
                    report.getWarnings().add("Unknown nutrient ignored for " + raw.getSourceRecordId() + ": " + entry.getKey());
                }
            }
        }
    }

    private void validateNutrients(Map<String, BigDecimal> nutrients, String sourceRecordId, ValidationReport report) {
        if (nutrients == null) return;
        
        for (Map.Entry<String, BigDecimal> entry : nutrients.entrySet()) {
            if (entry.getValue() != null && entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Negative nutrient value for " + entry.getKey());
            }
        }
        
        BigDecimal p = nutrients.getOrDefault("PROTEIN", BigDecimal.ZERO);
        BigDecimal c = nutrients.getOrDefault("CARBOHYDRATE", BigDecimal.ZERO);
        BigDecimal f = nutrients.getOrDefault("FAT", BigDecimal.ZERO);
        BigDecimal cal = nutrients.getOrDefault("CALORIES", BigDecimal.ZERO);
        
        // 4 kcal/g protein, 4 kcal/g carb, 9 kcal/g fat
        BigDecimal estimated = p.multiply(new BigDecimal("4"))
                .add(c.multiply(new BigDecimal("4")))
                .add(f.multiply(new BigDecimal("9")));
                
        // Allow 20% tolerance for energy mismatch (fiber, specific energy factors)
        if (cal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal diff = estimated.subtract(cal).abs();
            if (diff.divide(cal, 2, java.math.RoundingMode.HALF_UP).compareTo(new BigDecimal("0.20")) > 0) {
                report.getWarnings().add("Energy mismatch for " + sourceRecordId + " (Calculated: " + estimated + ", Source: " + cal + ")");
            }
        }
    }
}
