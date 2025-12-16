package com.resume.resumematching.service;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.dto.subscription.CreateSubscriptionRequest;
import com.resume.resumematching.dto.subscription.SubscriptionResponse;
import com.resume.resumematching.entity.Plan;
import com.resume.resumematching.entity.Subscription;
import com.resume.resumematching.entity.Tenant;
import com.resume.resumematching.entity.UsageCounter;
import com.resume.resumematching.enums.BillingCycle;
import com.resume.resumematching.enums.SubscriptionStatus;
import com.resume.resumematching.repository.PlanRepository;
import com.resume.resumematching.repository.SubscriptionRepository;
import com.resume.resumematching.repository.TenantRepository;
import com.resume.resumematching.repository.UsageCounterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;
    private final UsageCounterRepository usageCounterRepository;

    /* -------------------------------------------------
       CREATE SUBSCRIPTION (ADMIN ONLY)
    ------------------------------------------------- */

    public SubscriptionResponse createSubscription(CreateSubscriptionRequest request) {

        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new RuntimeException("Superuser cannot subscribe to plans");
        }

        // 🔐 ROLE CHECK — ADMIN ONLY
        if (!SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            throw new RuntimeException("Only ADMIN can subscribe to a plan");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // 1️⃣ Expire existing active subscription
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

        // 3️⃣ Create subscription
        Subscription subscription = Subscription.builder()
                .tenant(tenant)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Subscription saved = subscriptionRepository.save(subscription);

        // 4️⃣ Create usage counter (RESET USAGE)
        UsageCounter usage = UsageCounter.builder()
                .tenant(tenant)
                .subscription(saved)
                .resumeUsed(0)
                .jdUsed(0)
                .matchUsed(0)
                .build();

        usageCounterRepository.save(usage);

        return new SubscriptionResponse(
                saved.getId(),
                tenantId,
                plan.getName(),
                saved.getStatus(),
                saved.getStartDate(),
                saved.getEndDate()
        );
    }

    /* -------------------------------------------------
       FETCH ACTIVE SUBSCRIPTION (USED BY USAGE CHECKS)
    ------------------------------------------------- */

    public Subscription getActiveSubscription(Long tenantId) {

        Subscription subscription = subscriptionRepository
                .findByTenantIdAndStatus(tenantId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription"));

        // ⛔ AUTO-EXPIRE IF DATE PASSED
        if (subscription.getEndDate().isBefore(LocalDate.now())) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(subscription);
            throw new RuntimeException("Subscription expired");
        }

        return subscription;
    }
}
