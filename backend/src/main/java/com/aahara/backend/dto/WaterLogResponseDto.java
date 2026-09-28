package com.aahara.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class WaterLogResponseDto {
    private String id;
    private String userId;
    private LocalDate entryDate;
    private Integer amountMl;
    private String clientId;
    private String syncId;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
