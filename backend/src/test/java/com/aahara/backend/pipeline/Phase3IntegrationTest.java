package com.aahara.backend.pipeline;

import com.aahara.backend.dto.FoodListDto;
import com.aahara.backend.dto.PaginatedResponse;
import com.aahara.backend.entity.Food;
import com.aahara.backend.entity.FoodAlias;
import com.aahara.backend.entity.FoodNutrient;
import com.aahara.backend.entity.NutrientType;
import com.aahara.backend.repository.FoodAliasRepository;
import com.aahara.backend.repository.FoodNutrientRepository;
import com.aahara.backend.repository.FoodRepository;
import com.aahara.backend.service.FoodService;
import com.aahara.backend.util.StringNormalizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class Phase3IntegrationTest {

    @Autowired
    private DataImportService dataImportService;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodNutrientRepository nutrientRepository;

    @Autowired
    private FoodAliasRepository aliasRepository;

    @Autowired
    private FoodService foodService;

    @Test
    public void testStringNormalization() {
        assertEquals("idli", StringNormalizer.normalize("  IDLI  "));
        assertEquals("chicken cooked", StringNormalizer.normalize("Chicken   Cooked"));
    }

    @Test
    public void testValidImport_SavesDataAndAliases() {
        DataImportService.RawFoodRecord record = DataImportService.RawFoodRecord.builder()
                .sourceRecordId("IFCT_001")
                .name("Idli")
                .tamilName("இட்லி")
                .foodState("COOKED")
                .aliases(Arrays.asList("idly", "steamed rice cake"))
                .nutrientsPer100g(Map.of(
                        "CALORIES", new BigDecimal("142.0"),
                        "PROTEIN", new BigDecimal("4.0"),
                        "VITAMIN_C", new BigDecimal("0.5")
                ))
                .build();

        DataImportService.ImportRequest req = DataImportService.ImportRequest.builder()
                .source("IFCT 2017")
                .datasetVersion("v1")
                .records(Collections.singletonList(record))
                .build();

        DataImportService.ValidationReport report = dataImportService.runImportPipeline(req);
        assertEquals(1, report.getRecordsAccepted());
        assertEquals(0, report.getRecordsRejected());

        Optional<Food> savedFood = foodRepository.findBySourceAndSourceRecordIdAndDatasetVersion("IFCT 2017", "IFCT_001", "v1");
        assertTrue(savedFood.isPresent());
        Food food = savedFood.get();
        assertEquals("Idli", food.getName());
        assertEquals("இட்லி", food.getTamilName());
        assertEquals(Food.FoodState.COOKED, food.getFoodState());

        List<FoodAlias> aliases = aliasRepository.findByFoodId(food.getId());
        assertEquals(2, aliases.size());
        assertTrue(aliases.stream().anyMatch(a -> a.getNormalizedAlias().equals("idly")));

        List<FoodNutrient> nutrients = nutrientRepository.findByFoodId(food.getId());
        assertEquals(3, nutrients.size());
        assertTrue(nutrients.stream().anyMatch(n -> n.getNutrientType() == NutrientType.VITAMIN_C));
    }

    @Test
    public void testImport_Idempotency() {
        DataImportService.RawFoodRecord record = DataImportService.RawFoodRecord.builder()
                .sourceRecordId("dup_test")
                .name("Test Food")
                .build();

        DataImportService.ImportRequest req = DataImportService.ImportRequest.builder()
                .source("TestSource")
                .datasetVersion("v1")
                .records(Collections.singletonList(record))
                .build();

        dataImportService.runImportPipeline(req);
        long count1 = foodRepository.count();

        dataImportService.runImportPipeline(req);
        long count2 = foodRepository.count();

        assertEquals(count1, count2, "Importing the same dataset should be idempotent");
    }

    @Test
    public void testValidation_NegativeNutrientRejection() {
        DataImportService.RawFoodRecord record = DataImportService.RawFoodRecord.builder()
                .sourceRecordId("bad_nutrients")
                .name("Bad Food")
                .nutrientsPer100g(Map.of("CALORIES", new BigDecimal("-10")))
                .build();

        DataImportService.ImportRequest req = DataImportService.ImportRequest.builder()
                .source("TestSource")
                .records(Collections.singletonList(record))
                .build();

        DataImportService.ValidationReport report = dataImportService.runImportPipeline(req);
        assertEquals(0, report.getRecordsAccepted());
        assertEquals(1, report.getRecordsRejected());
        assertTrue(report.getErrors().get(0).contains("Negative nutrient"));
    }

    @Test
    public void testSearchFoods_ByTamilAndEnglish() {
        Food food = new Food();
        food.setId("search_test_1");
        food.setName("Dosa");
        food.setTamilName("தோசை");
        food.setStatus(Food.FoodStatus.ACTIVE);
        foodRepository.save(food);

        PaginatedResponse<FoodListDto> enRes = foodService.searchFoods("dosa", 0, 10);
        assertTrue(enRes.getItems().stream().anyMatch(f -> f.getId().equals("search_test_1")));

        PaginatedResponse<FoodListDto> taRes = foodService.searchFoods("தோசை", 0, 10);
        assertTrue(taRes.getItems().stream().anyMatch(f -> f.getId().equals("search_test_1")));
    }
}
