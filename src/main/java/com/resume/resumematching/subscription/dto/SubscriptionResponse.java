package com.resume.resumematching.subscription.dto;

import com.resume.resumematching.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SubscriptionResponse {

    private Long id;
    private Long tenantId;
    private String planName;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}
