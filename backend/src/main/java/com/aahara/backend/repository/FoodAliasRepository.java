package com.aahara.backend.repository;

import com.aahara.backend.entity.FoodAlias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodAliasRepository extends JpaRepository<FoodAlias, String> {
    Page<FoodAlias> findByNormalizedAliasStartingWith(String normalizedPrefix, Pageable pageable);
    Page<FoodAlias> findByNormalizedAliasContaining(String normalizedSnippet, Pageable pageable);
    List<FoodAlias> findByFoodId(String foodId);
}
