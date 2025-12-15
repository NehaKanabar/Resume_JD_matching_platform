package com.resume.resumematching.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

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
}
