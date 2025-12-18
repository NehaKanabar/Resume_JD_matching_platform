package com.resume.resumematching.dto.payment;

import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull Long invoiceId
) {}

