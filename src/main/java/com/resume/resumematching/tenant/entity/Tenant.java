package com.resume.resumematching.tenant.entity;

import com.resume.resumematching.common.audit.Auditable;
import com.resume.resumematching.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    private String stripeCustomerId;

    @PrePersist
    public void onCreate() {
        if (this.status == null) {
            this.status = TenantStatus.ACTIVE;
        }
    }
}
