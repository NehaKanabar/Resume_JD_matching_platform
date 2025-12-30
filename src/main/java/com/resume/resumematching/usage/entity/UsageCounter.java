package com.resume.resumematching.usage.entity;

import com.resume.resumematching.subscription.entity.Subscription;
import com.resume.resumematching.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.*;
import com.resume.resumematching.common.audit.Auditable;

@Entity
@Table(name = "usage_counter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsageCounter extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(nullable = false)
    private int resumeUsed;

    @Column(nullable = false)
    private int jdUsed;

    @Column(nullable = false)
    private int matchUsed;
}
