package com.resume.resumematching.usage;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.usage.dto.UsageResponse;
import com.resume.resumematching.subscription.entity.Subscription;
import com.resume.resumematching.usage.entity.UsageCounter;
import com.resume.resumematching.enums.SubscriptionStatus;
import com.resume.resumematching.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsageService {

    private final UsageCounterRepository usageRepo;
    private final SubscriptionService subscriptionService;

    public UsageResponse getCurrentUsage() {

        Long tenantId = TenantContext.getTenantId();
//        if (tenantId == null) {
//            throw new RuntimeException("Superuser must specify tenant");
//        }

        Subscription sub = subscriptionService.getActiveSubscription(tenantId);

        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new RuntimeException("Subscription not active");
        }

        UsageCounter usage = usageRepo.findBySubscription(sub)
                .orElseThrow(() -> new RuntimeException("Usage counter not found"));

        int resumeLimit = sub.getPlan().getResumeLimit();
        int jdLimit = sub.getPlan().getJdLimit();
        int matchLimit = sub.getPlan().getMatchLimit();

        boolean nearLimit =
                usage.getResumeUsed() >= (resumeLimit * 0.8)
                        || usage.getJdUsed() >= (jdLimit * 0.8)
                        || usage.getMatchUsed() >= (matchLimit * 0.8);

        return new UsageResponse(
                tenantId,
                sub.getPlan().getName(),
                usage.getResumeUsed(),
                resumeLimit,
                usage.getJdUsed(),
                jdLimit,
                usage.getMatchUsed(),
                matchLimit,
                nearLimit
        );
    }
}
