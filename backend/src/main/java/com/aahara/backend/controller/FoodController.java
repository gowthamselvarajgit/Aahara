package com.aahara.backend.controller;

import com.aahara.backend.dto.FoodDetailDto;
import com.aahara.backend.dto.FoodListDto;
import com.aahara.backend.dto.PaginatedResponse;
import com.aahara.backend.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {
    
    private final FoodService foodService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<FoodListDto>> searchFoods(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        return ResponseEntity.ok(foodService.searchFoods(q, page, size));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FoodDetailDto> getFoodDetail(@PathVariable String id) {
        return foodService.getFoodDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
