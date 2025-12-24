package com.resume.resumematching.payment.dto;

import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull Long invoiceId
) {}

