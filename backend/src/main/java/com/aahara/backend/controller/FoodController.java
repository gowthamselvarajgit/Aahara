package com.aahara.backend.controller;
import com.aahara.backend.dto.FoodDetailDto;
import com.aahara.backend.dto.FoodListDto;
import com.aahara.backend.dto.PaginatedResponse;
import com.aahara.backend.entity.Food;
import com.aahara.backend.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {
    
    private final FoodRepository foodRepository;

    @GetMapping
    public ResponseEntity<PaginatedResponse<FoodListDto>> getFoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
            
        // Hard-cap page size to prevent abuse
        int safeSize = Math.min(size, 100);
        Page<Food> foodPage = foodRepository.findAll(PageRequest.of(page, safeSize));
        
        List<FoodListDto> dtos = foodPage.getContent().stream()
                .map(f -> FoodListDto.builder()
                        .id(f.getId())
                        .name(f.getName())
                        .brand(f.getBrand())
                        .build())
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(PaginatedResponse.<FoodListDto>builder()
                .items(dtos)
                .page(foodPage.getNumber())
                .pageSize(foodPage.getSize())
                .totalItems(foodPage.getTotalElements())
                .totalPages(foodPage.getTotalPages())
                .build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FoodDetailDto> getFoodDetail(@PathVariable String id) {
        return foodRepository.findById(id)
                .map(f -> ResponseEntity.ok(FoodDetailDto.builder()
                        .id(f.getId())
                        .name(f.getName())
                        .brand(f.getBrand())
                        .source(f.getSource())
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }
}
