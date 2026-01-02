package com.resume.resumematching.subscription;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.enums.PlanStatus;
import com.resume.resumematching.invoice.InvoiceRepository;
import com.resume.resumematching.invoice.entity.Invoice;
import com.resume.resumematching.plan.PlanRepository;
import com.resume.resumematching.plan.entity.Plan;
import com.resume.resumematching.security.SecurityUtils;
import com.resume.resumematching.subscription.dto.CreateSubscriptionRequest;
import com.resume.resumematching.subscription.dto.SubscriptionResponse;
import com.resume.resumematching.enums.BillingCycle;
import com.resume.resumematching.enums.InvoiceStatus;
import com.resume.resumematching.enums.SubscriptionStatus;
import com.resume.resumematching.subscription.entity.Subscription;
import com.resume.resumematching.tenant.TenantRepository;
import com.resume.resumematching.tenant.entity.Tenant;
import com.resume.resumematching.usage.UsageCounterRepository;
import com.resume.resumematching.usage.entity.UsageCounter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;
    private final UsageCounterRepository usageCounterRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public SubscriptionResponse createSubscription(CreateSubscriptionRequest request) {

        Long tenantId = TenantContext.getTenantId();

        if (SecurityUtils.isSuperUser()) {
            throw new RuntimeException("Superuser cannot subscribe to plans");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        // ONLY ACTIVE PLAN CAN BE SUBSCRIBED
        Plan plan = planRepository
                .findByIdAndStatus(request.getPlanId(), PlanStatus.ACTIVE)
                .orElseThrow(() ->
                        new RuntimeException("Selected plan is not active")
                );

        // Expire existing active subscription (if any)
        subscriptionRepository
                .findByTenantIdAndStatus(tenantId, SubscriptionStatus.ACTIVE)
                .ifPresent(existing -> {
                    existing.setStatus(SubscriptionStatus.EXPIRED);
                    subscriptionRepository.save(existing);
                });

        // Calculate subscription dates
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = request.getBillingCycle() == BillingCycle.YEARLY
                ? startDate.plusYears(1)
                : startDate.plusMonths(1);

        // Create new subscription
        Subscription subscription = Subscription.builder()
                .tenant(tenant)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Reset usage counter
        UsageCounter usage = UsageCounter.builder()
                .tenant(tenant)
                .subscription(savedSubscription)
                .resumeUsed(0)
                .jdUsed(0)
                .matchUsed(0)
                .build();

        usageCounterRepository.save(usage);

        // Create invoice
        BigDecimal amount = request.getBillingCycle() == BillingCycle.YEARLY
                ? plan.getPriceYearly()
                : plan.getPriceMonthly();

        Invoice invoice = Invoice.builder()
                .tenantId(tenantId)
                .subscriptionId(savedSubscription.getId())
                .amount(amount)
                .status(InvoiceStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(7))
                .build();

        invoiceRepository.save(invoice);

        return new SubscriptionResponse(
                savedSubscription.getId(),
                tenantId,
                plan.getName() + " v" + plan.getVersion(),
                savedSubscription.getStatus(),
                savedSubscription.getStartDate(),
                savedSubscription.getEndDate()
        );
    }


    //  FETCH ACTIVE SUBSCRIPTION
    public Subscription getActiveSubscription(Long tenantId) {

        Subscription subscription = subscriptionRepository
                .findByTenantIdAndStatus(tenantId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription"));

        // AUTO-EXPIRE IF DATE PASSED
        if (subscription.getEndDate().isBefore(LocalDate.now())) {
            subscription.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(subscription);
            throw new RuntimeException("Subscription expired");
        }

        return subscription;
    }
}
