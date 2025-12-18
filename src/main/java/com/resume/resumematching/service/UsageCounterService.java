package com.resume.resumematching.service;

import com.resume.resumematching.entity.Subscription;
import com.resume.resumematching.entity.UsageCounter;
import com.resume.resumematching.enums.SubscriptionStatus;
import com.resume.resumematching.repository.UsageCounterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UsageCounterService {

    private final UsageCounterRepository usageRepo;
    private final SubscriptionService subscriptionService;

   //common
    private UsageCounter getUsageForActiveSubscription(Long tenantId) {

        Subscription subscription =
                subscriptionService.getActiveSubscription(tenantId);

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Subscription is not active");
        }

        return usageRepo.findBySubscription(subscription)
                .orElseThrow(() ->
                        new IllegalStateException("Usage counter not found for active subscription"));
    }

    // resume usage
    public void checkResumeLimit(Long tenantId) {

        UsageCounter usage = getUsageForActiveSubscription(tenantId);
        int limit = usage.getSubscription().getPlan().getResumeLimit();

        if (usage.getResumeUsed() >= limit) {
            throw new IllegalStateException("Resume upload limit exceeded");
        }
    }

    public void incrementResume(Long tenantId) {

        UsageCounter usage = getUsageForActiveSubscription(tenantId);
        usage.setResumeUsed(usage.getResumeUsed() + 1);
    }

   //JD usage
    public void checkJdLimit(Long tenantId) {

        UsageCounter usage = getUsageForActiveSubscription(tenantId);
        int limit = usage.getSubscription().getPlan().getJdLimit();

        if (usage.getJdUsed() >= limit) {
            throw new IllegalStateException("JD upload limit exceeded");
        }
    }

    public void incrementJd(Long tenantId) {

        UsageCounter usage = getUsageForActiveSubscription(tenantId);
        usage.setJdUsed(usage.getJdUsed() + 1);
    }

    // match usage
    public void checkMatchLimit(Long tenantId) {

        UsageCounter usage = getUsageForActiveSubscription(tenantId);
        int limit = usage.getSubscription().getPlan().getMatchLimit();

        if (usage.getMatchUsed() >= limit) {
            throw new IllegalStateException("Match limit exceeded");
        }
    }

    public void incrementMatch(Long tenantId) {

        UsageCounter usage = getUsageForActiveSubscription(tenantId);
        usage.setMatchUsed(usage.getMatchUsed() + 1);
    }
}
