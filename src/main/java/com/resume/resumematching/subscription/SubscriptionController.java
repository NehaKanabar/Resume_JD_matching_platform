package com.resume.resumematching.subscription;

import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.subscription.dto.CreateSubscriptionRequest;
import com.resume.resumematching.subscription.dto.SubscriptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
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
