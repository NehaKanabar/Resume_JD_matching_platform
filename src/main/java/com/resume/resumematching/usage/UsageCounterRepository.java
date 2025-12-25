package com.resume.resumematching.usage;

import com.resume.resumematching.subscription.entity.Subscription;
import com.resume.resumematching.tenant.entity.Tenant;
import com.resume.resumematching.usage.entity.UsageCounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsageCounterRepository extends JpaRepository<UsageCounter, Long> {

    // Used internally for limit checks
    Optional<UsageCounter> findBySubscription(Subscription subscription);

    // Useful for analytics / admin views
    Optional<UsageCounter> findByTenant(Tenant tenant);
}
