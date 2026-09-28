package com.aahara.backend.repository;

import com.aahara.backend.entity.FoodPortion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodPortionRepository extends JpaRepository<FoodPortion, String> {
    List<FoodPortion> findByFoodId(String foodId);
}
