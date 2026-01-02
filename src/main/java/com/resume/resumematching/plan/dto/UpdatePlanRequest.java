package com.resume.resumematching.plan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdatePlanRequest {

    @Min(1)
    private Integer resumeLimit;

    @Min(1)
    private Integer jdLimit;

    @Min(1)
    private  Integer matchLimit;

    private BigDecimal priceMonthly;

    private BigDecimal priceYearly;
}
