package com.aahara.backend.service;

import com.aahara.backend.dto.FoodDetailDto;
import com.aahara.backend.dto.FoodListDto;
import com.aahara.backend.dto.PaginatedResponse;
import com.aahara.backend.entity.Food;
import com.aahara.backend.entity.FoodAlias;
import com.aahara.backend.entity.FoodNutrient;
import com.aahara.backend.entity.FoodPortion;
import com.aahara.backend.repository.FoodAliasRepository;
import com.aahara.backend.repository.FoodNutrientRepository;
import com.aahara.backend.repository.FoodPortionRepository;
import com.aahara.backend.repository.FoodRepository;
import com.aahara.backend.util.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodPortionRepository portionRepository;
    private final FoodNutrientRepository nutrientRepository;
    private final FoodAliasRepository aliasRepository;

    @Transactional(readOnly = true)
    public PaginatedResponse<FoodListDto> searchFoods(String query, int page, int size) {
        int safeSize = Math.min(size, 100);
        PageRequest pageRequest = PageRequest.of(page, safeSize);
        Page<Food> foodPage;

        if (query == null || query.trim().isEmpty()) {
            foodPage = foodRepository.findAll(pageRequest);
        } else {
            String normalizedQuery = StringNormalizer.normalize(query);
            // Search using repository
            foodPage = foodRepository.searchFoodsByStatus(normalizedQuery, Food.FoodStatus.ACTIVE, pageRequest);
        }

        List<FoodListDto> dtos = foodPage.getContent().stream()
                .map(this::mapToListDto)
                .collect(Collectors.toList());

        return PaginatedResponse.<FoodListDto>builder()
                .items(dtos)
                .page(foodPage.getNumber())
                .pageSize(foodPage.getSize())
                .totalItems(foodPage.getTotalElements())
                .totalPages(foodPage.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<FoodDetailDto> getFoodDetail(String id) {
        return foodRepository.findById(id).map(this::mapToDetailDto);
    }

    private FoodListDto mapToListDto(Food f) {
        return FoodListDto.builder()
                .id(f.getId())
                .name(f.getName())
                .tamilName(f.getTamilName())
                .brand(f.getBrand())
                .category(f.getCategory())
                .foodState(f.getFoodState() != null ? f.getFoodState().name() : null)
                .status(f.getStatus() != null ? f.getStatus().name() : null)
                .source(f.getSource())
                .build();
    }

    private FoodDetailDto mapToDetailDto(Food f) {
        List<FoodAlias> aliases = aliasRepository.findByFoodId(f.getId());
        List<String> aliasStrings = aliases.stream().map(FoodAlias::getAlias).collect(Collectors.toList());

        List<FoodPortion> portions = portionRepository.findByFoodId(f.getId());
        List<FoodDetailDto.PortionDto> portionDtos = portions.stream()
                .map(p -> FoodDetailDto.PortionDto.builder()
                        .id(p.getId())
                        .description(p.getDescription())
                        .gramWeight(p.getGramWeight())
                        .build())
                .collect(Collectors.toList());

        List<FoodNutrient> nutrients = nutrientRepository.findByFoodId(f.getId());
        Map<String, BigDecimal> nutrientMap = nutrients.stream()
                .collect(Collectors.toMap(
                        n -> n.getNutrientType().name(),
                        FoodNutrient::getAmountPer100g
                ));

        return FoodDetailDto.builder()
                .id(f.getId())
                .name(f.getName())
                .tamilName(f.getTamilName())
                .searchAliases(aliasStrings)
                .brand(f.getBrand())
                .category(f.getCategory())
                .foodState(f.getFoodState() != null ? f.getFoodState().name() : null)
                .source(f.getSource())
                .datasetVersion(f.getDatasetVersion())
                .sourceUrl(f.getSourceUrl())
                .license(f.getLicense())
                .status(f.getStatus() != null ? f.getStatus().name() : null)
                .isVerified(f.getIsVerified())
                .ownerUserId(f.getOwnerUserId())
                .portions(portionDtos)
                .nutrientsPer100g(nutrientMap)
                .build();
    }
}
