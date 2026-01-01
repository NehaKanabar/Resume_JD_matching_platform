package com.resume.resumematching.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanResponse {

    private Long id;
    private String name;
    private int resumeLimit;
    private int jdLimit;
    private int matchLimit;
    private BigDecimal priceMonthly;
    private BigDecimal priceYearly;
}
