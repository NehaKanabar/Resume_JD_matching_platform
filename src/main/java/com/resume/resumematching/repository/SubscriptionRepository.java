package com.resume.resumematching.repository;

import com.resume.resumematching.entity.Subscription;
import com.resume.resumematching.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByTenantIdAndStatus(Long tenantId, SubscriptionStatus status);

    List<Subscription> findByTenantId(Long tenantId);

    List<Subscription> findByStatus(SubscriptionStatus status);
}
