package com.aahara.backend.service;

import com.aahara.backend.dto.DiaryEntryRequestDto;
import com.aahara.backend.dto.DiaryEntryResponseDto;
import com.aahara.backend.entity.*;
import com.aahara.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DiaryEntryServiceIntegrationTest {

    @Autowired
    private DiaryEntryService diaryService;
    @Autowired
    private DiaryEntryRepository diaryRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodNutrientRepository nutrientRepository;
    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Food testFood;

    @BeforeEach
    public void setup() {
        testUser = userRepository.save(User.builder()
            .id(UUID.randomUUID().toString())
            .email("test@test.com")
            .passwordHash("hash")
            .build());

        testFood = foodRepository.save(Food.builder()
            .id(UUID.randomUUID().toString())
            .name("Apple")
            .source("USDA")
            .datasetVersion("v1")
            .build());

        nutrientRepository.save(FoodNutrient.builder()
            .id(UUID.randomUUID().toString())
            .foodId(testFood.getId())
            .nutrientType(NutrientType.CALORIES)
            .amountPer100g(new BigDecimal("52.0"))
            .build());
    }

    @Test
    public void testCreateEntry_ServerCalculatesNutritionAndCreatesSnapshot() {
        DiaryEntryRequestDto req = new DiaryEntryRequestDto();
        req.setEntryDate(LocalDate.now());
        req.setMealType(MealType.SNACK);
        req.setFoodId(testFood.getId());
        req.setQuantity(new BigDecimal("150")); // 150g -> 1.5 * 52 = 78 calories

        DiaryEntryResponseDto res = diaryService.createEntry(testUser.getId(), req);
        
        assertNotNull(res.getId());
        assertEquals(0, res.getCaloriesSnapshot().compareTo(new BigDecimal("78.00")));
        assertEquals("Apple", res.getFoodNameSnapshot());
        assertTrue(res.getNutrientsSnapshotJson().contains("\"CALORIES\""));
        assertTrue(res.getNutrientsSnapshotJson().contains("78.00"));
        assertTrue(res.getNutrientsSnapshotJson().contains("\"schemaVersion\":1"));
        assertTrue(res.getNutrientsSnapshotJson().contains("\"USDA\""));
        assertTrue(res.getNutrientsSnapshotJson().contains("\"v1\""));
    }

    @Test
    public void testCreateEntry_MasterFoodMutationDoesNotAlterSnapshot() {
        DiaryEntryRequestDto req = new DiaryEntryRequestDto();
        req.setEntryDate(LocalDate.now());
        req.setMealType(MealType.SNACK);
        req.setFoodId(testFood.getId());
        req.setQuantity(new BigDecimal("100"));

        DiaryEntryResponseDto res = diaryService.createEntry(testUser.getId(), req);
        DiaryEntry savedEntry = diaryRepository.findById(res.getId()).get();

        String originalSnapshot = savedEntry.getNutrientsSnapshotJson();

        // Mutate Master Food
        testFood.setName("Green Apple");
        foodRepository.save(testFood);

        // Fetch again, snapshot should be untouched
        DiaryEntry fetchedEntry = diaryRepository.findById(res.getId()).get();
        assertEquals(originalSnapshot, fetchedEntry.getNutrientsSnapshotJson());
        assertTrue(fetchedEntry.getNutrientsSnapshotJson().contains("\"Apple\"")); // Old name
        assertFalse(fetchedEntry.getNutrientsSnapshotJson().contains("\"Green Apple\""));
    }

    @Test
    public void testCreateEntry_Idempotency() {
        DiaryEntryRequestDto req = new DiaryEntryRequestDto();
        req.setEntryDate(LocalDate.now());
        req.setMealType(MealType.SNACK);
        req.setFoodId(testFood.getId());
        req.setQuantity(new BigDecimal("100"));
        req.setClientId("client-uuid-123");

        DiaryEntryResponseDto res1 = diaryService.createEntry(testUser.getId(), req);
        DiaryEntryResponseDto res2 = diaryService.createEntry(testUser.getId(), req);
        
        assertEquals(res1.getId(), res2.getId()); // Should return the exact same entry without duplicating
    }
}
