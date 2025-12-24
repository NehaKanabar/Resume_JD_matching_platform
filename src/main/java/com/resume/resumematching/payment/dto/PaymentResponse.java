package com.resume.resumematching.payment.dto;

import com.resume.resumematching.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(
        Long invoiceId,
        PaymentStatus status,
        String stripePaymentIntentId,
        BigDecimal amount
) {}

