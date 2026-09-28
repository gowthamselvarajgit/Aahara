package com.aahara.backend.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CalculationRequestDto {
    @NotNull private LocalDate dateOfBirth;
    @NotNull private String sex;
    @NotNull @DecimalMin("50.0") @DecimalMax("300.0") private BigDecimal heightCm;
    @NotNull @DecimalMin("20.0") @DecimalMax("400.0") private BigDecimal currentWeightKg;
    @NotNull private String goal;
    @NotNull private String activityLevel;
}
