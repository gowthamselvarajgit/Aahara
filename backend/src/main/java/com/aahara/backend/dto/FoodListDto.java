package com.aahara.backend.dto;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class FoodListDto {
    private String id;
    private String name;
    private String brand;
}
