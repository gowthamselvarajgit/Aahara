package com.aahara.backend.repository;
import com.aahara.backend.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FoodRepository extends JpaRepository<Food, String> {}
