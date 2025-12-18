package com.resume.resumematching.dto.payment;

import com.resume.resumematching.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(
        Long invoiceId,
        PaymentStatus status,
        String stripePaymentIntentId,
        BigDecimal amount
) {}

