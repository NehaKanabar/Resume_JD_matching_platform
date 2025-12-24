package com.resume.resumematching.controller;

import com.resume.resumematching.dto.common.ApiResponse;
import com.resume.resumematching.dto.subscription.CreateSubscriptionRequest;
import com.resume.resumematching.dto.subscription.SubscriptionResponse;
import com.resume.resumematching.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // ADMIN → Subscribe to plan
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionResponse>> subscribe(
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {

        SubscriptionResponse subscription =
                subscriptionService.createSubscription(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Subscription created successfully",
                        subscription
                )
        );
    }
}
