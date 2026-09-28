package com.aahara.backend.repository;

import com.aahara.backend.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {

    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.tamilName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Food> searchFoods(@Param("query") String query, Pageable pageable);

    @Query("SELECT f FROM Food f WHERE f.status = :status AND (LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.tamilName) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Food> searchFoodsByStatus(@Param("query") String query, @Param("status") Food.FoodStatus status, Pageable pageable);
    
    Optional<Food> findBySourceAndSourceRecordIdAndDatasetVersion(String source, String sourceRecordId, String datasetVersion);
}
