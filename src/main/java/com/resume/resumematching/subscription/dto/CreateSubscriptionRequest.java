package com.resume.resumematching.subscription.dto;

import com.resume.resumematching.enums.BillingCycle;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubscriptionRequest {

    @NotNull
    private Long planId;

    @NotNull
    private BillingCycle billingCycle; // MONTHLY / YEARLY
}
