package com.resume.resumematching.payment.entity;

import com.resume.resumematching.enums.PaymentStatus;
import com.resume.resumematching.invoice.entity.Invoice;
import jakarta.persistence.*;
import lombok.*;
import com.resume.resumematching.common.audit.Auditable;
import java.math.BigDecimal;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;
}
