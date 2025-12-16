package com.resume.resumematching.controller;

import com.resume.resumematching.dto.subscription.CreateSubscriptionRequest;
import com.resume.resumematching.dto.subscription.SubscriptionResponse;
import com.resume.resumematching.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SubscriptionResponse subscribe(
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        return subscriptionService.createSubscription(request);
    }
}
