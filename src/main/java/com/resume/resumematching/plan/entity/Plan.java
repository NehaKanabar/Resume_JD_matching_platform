package com.resume.resumematching.plan.entity;

import com.resume.resumematching.common.audit.Auditable;
import com.resume.resumematching.enums.PlanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "plan",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "version"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;   // BASIC, PRO, ENTERPRISE

    @Column(nullable = false)
    private int version;   // 1, 2, 3 ...

    @Column(nullable = false)
    private int resumeLimit;

    @Column(nullable = false)
    private int jdLimit;

    @Column(nullable = false)
    private int matchLimit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMonthly;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceYearly;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanStatus status; // DRAFT, ACTIVE, PAUSED
}
