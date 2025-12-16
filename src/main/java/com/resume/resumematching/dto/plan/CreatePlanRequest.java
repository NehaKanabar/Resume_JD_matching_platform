package com.resume.resumematching.dto.plan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePlanRequest {

    @NotBlank
    private String name;

    @Min(1)
    private int resumeLimit;

    @Min(1)
    private int jdLimit;

    @Min(1)
    private int matchLimit;

    @NotNull
    private BigDecimal priceMonthly;

    @NotNull
    private BigDecimal priceYearly;
}
