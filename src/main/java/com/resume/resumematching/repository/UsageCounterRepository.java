package com.resume.resumematching.repository;

import com.resume.resumematching.entity.Subscription;
import com.resume.resumematching.entity.Tenant;
import com.resume.resumematching.entity.UsageCounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsageCounterRepository extends JpaRepository<UsageCounter, Long> {

    // Used internally for limit checks
    Optional<UsageCounter> findBySubscription(Subscription subscription);

    // Useful for analytics / admin views
    Optional<UsageCounter> findByTenant(Tenant tenant);
}
