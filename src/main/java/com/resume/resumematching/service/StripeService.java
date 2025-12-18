package com.resume.resumematching.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {

    public String createPayment(BigDecimal amount) {

        //  Simulated Stripe payment
        boolean success = Math.random() > 0.1; // 90% success

        if (!success) {
            throw new RuntimeException("Stripe payment failed");
        }

        return "pi_" + System.currentTimeMillis();
    }
}
