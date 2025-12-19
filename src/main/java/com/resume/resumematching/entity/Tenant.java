package com.resume.resumematching.entity;

import com.resume.resumematching.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "tenant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    private String stripeCustomerId;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = TenantStatus.ACTIVE;
        }
    }
}
