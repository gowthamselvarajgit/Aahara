package com.aahara.backend.repository;

import com.aahara.backend.entity.FoodNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodNutrientRepository extends JpaRepository<FoodNutrient, String> {
    List<FoodNutrient> findByFoodId(String foodId);
}
