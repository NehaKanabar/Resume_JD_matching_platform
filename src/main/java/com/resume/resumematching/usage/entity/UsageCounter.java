package com.resume.resumematching.usage.entity;

import com.resume.resumematching.subscription.entity.Subscription;
import com.resume.resumematching.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usage_counter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsageCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Tenant tenant;

    @OneToOne(optional = false)
    private Subscription subscription;

    @Column(nullable = false)
    private int resumeUsed;

    @Column(nullable = false)
    private int jdUsed;

    @Column(nullable = false)
    private int matchUsed;
}
