package com.aahara.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class FoodListDto {
    private String id;
    private String name;
    private String tamilName;
    private String brand;
    private String category;
    private String foodState;
    private String status;
    private String source;
}
