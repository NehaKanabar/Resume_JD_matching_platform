package com.resume.resumematching.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    //Create Stripe Customer for a Tenant (Company)
    public Customer createCustomer(String tenantName) throws StripeException {
        return Customer.create(
                CustomerCreateParams.builder()
                        .setName(tenantName)
                        .build()
        );
    }

    // Create Stripe PaymentIntent linked to a Customer
    public PaymentIntent createPaymentIntent(
            BigDecimal amount,
            String stripeCustomerId,
            Long invoiceId,
            Long tenantId
    ) throws StripeException {

        return PaymentIntent.create(
                PaymentIntentCreateParams.builder()
                        // Stripe expects smallest currency unit
                        .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                        .setCurrency("inr")
                        .setCustomer(stripeCustomerId)
                        // Metadata for traceability
                        .putMetadata("invoice_id", invoiceId.toString())
                        .putMetadata("tenant_id", tenantId.toString())
                        .build()
        );
    }
}
