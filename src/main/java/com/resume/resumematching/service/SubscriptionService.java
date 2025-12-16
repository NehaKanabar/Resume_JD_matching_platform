package com.resume.resumematching.service;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.dto.subscription.CreateSubscriptionRequest;
import com.resume.resumematching.dto.subscription.SubscriptionResponse;
import com.resume.resumematching.entity.Plan;
import com.resume.resumematching.entity.Subscription;
import com.resume.resumematching.entity.Tenant;
import com.resume.resumematching.enums.BillingCycle;
import com.resume.resumematching.enums.SubscriptionStatus;
import com.resume.resumematching.repository.PlanRepository;
import com.resume.resumematching.repository.SubscriptionRepository;
import com.resume.resumematching.repository.TenantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;

    @Transactional
    public SubscriptionResponse createSubscription(CreateSubscriptionRequest request) {

        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new RuntimeException("Superuser cannot subscribe to plans");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // 1️⃣ Expire existing subscription
        subscriptionRepository
                .findByTenantIdAndStatus(tenantId, SubscriptionStatus.ACTIVE)
                .ifPresent(existing -> {
                    existing.setStatus(SubscriptionStatus.EXPIRED);
                    subscriptionRepository.save(existing);
                });

        // 2️⃣ Calculate dates
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = request.getBillingCycle() == BillingCycle.YEARLY
                ? startDate.plusYears(1)
                : startDate.plusMonths(1);

        // 3️⃣ Create new subscription
        Subscription subscription = Subscription.builder()
                .tenant(tenant)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Subscription saved = subscriptionRepository.save(subscription);

        return new SubscriptionResponse(
                saved.getId(),
                tenantId,
                plan.getName(),
                saved.getStatus(),
                saved.getStartDate(),
                saved.getEndDate()
        );
    }
}
