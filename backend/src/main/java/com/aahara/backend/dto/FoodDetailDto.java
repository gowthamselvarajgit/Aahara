package com.aahara.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data @Builder
public class FoodDetailDto {
    private String id;
    private String name;
    private String tamilName;
    private List<String> searchAliases;
    private String brand;
    private String category;
    private String foodState;
    
    // Provenance
    private String source;
    private String datasetVersion;
    private String sourceUrl;
    private String license;
    private String status;
    private Boolean isVerified;
    private String ownerUserId;

    // Serving info
    private List<PortionDto> portions;
    
    // Nutrients
    private Map<String, BigDecimal> nutrientsPer100g;

    @Data @Builder
    public static class PortionDto {
        private String id;
        private String description;
        private BigDecimal gramWeight;
    }
}
