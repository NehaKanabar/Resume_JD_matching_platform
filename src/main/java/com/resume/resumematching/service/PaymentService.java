package com.resume.resumematching.service;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.dto.payment.PaymentResponse;
import com.resume.resumematching.entity.Invoice;
import com.resume.resumematching.entity.Payment;
import com.resume.resumematching.enums.InvoiceStatus;
import com.resume.resumematching.enums.PaymentStatus;
import com.resume.resumematching.repository.InvoiceRepository;
import com.resume.resumematching.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    public PaymentResponse payInvoice(Long invoiceId) throws StripeException {

        Long tenantId = TenantContext.getTenantId();

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (!invoice.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Unauthorized invoice access");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new RuntimeException("Invoice already paid");
        }

        //  Create Stripe PaymentIntent
        PaymentIntent intent = PaymentIntent.create(
                PaymentIntentCreateParams.builder()
                        .setAmount(invoice.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                        .setCurrency("inr")
                        .setPaymentMethod("pm_card_visa") // TEST CARD
                        .setConfirm(true)
                        .build()
        );

        // Save payment
        Payment payment = paymentRepository.save(
                Payment.builder()
                        .invoice(invoice)
                        .tenantId(tenantId)
                        .amount(invoice.getAmount())
                        .stripePaymentIntentId(intent.getId())
                        .status(PaymentStatus.SUCCESS)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        //  Mark invoice PAID
        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);

        return new PaymentResponse(
                payment.getInvoice().getId(),
                payment.getStatus(),
                payment.getStripePaymentIntentId(),
                payment.getAmount()
        );
    }
}