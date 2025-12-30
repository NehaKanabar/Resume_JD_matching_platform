package com.resume.resumematching.payment;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.invoice.InvoiceRepository;
import com.resume.resumematching.payment.dto.PaymentResponse;
import com.resume.resumematching.invoice.entity.Invoice;
import com.resume.resumematching.payment.entity.Payment;
import com.resume.resumematching.tenant.entity.Tenant;
import com.resume.resumematching.enums.InvoiceStatus;
import com.resume.resumematching.enums.PaymentStatus;
import com.resume.resumematching.tenant.TenantRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final TenantRepository tenantRepository;
    private final StripeService stripeService;

    public PaymentResponse payInvoice(Long invoiceId) throws StripeException {

        Long tenantId = TenantContext.getTenantId();

        // Fetch invoice
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Tenant validation
        if (!invoice.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Unauthorized invoice access");
        }

        // Invoice status validation
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new RuntimeException("Invoice already paid");
        }

        // Fetch tenant
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        // Create Stripe Customer if not exists
        String stripeCustomerId = tenant.getStripeCustomerId();

        if (stripeCustomerId == null) {
            Customer customer = stripeService.createCustomer(
                    tenant.getName()
            );
            stripeCustomerId = customer.getId();
            tenant.setStripeCustomerId(stripeCustomerId);
            tenantRepository.save(tenant);
        }

        // Create Stripe PaymentIntent
        PaymentIntent intent = stripeService.createPaymentIntent(
                invoice.getAmount(),
                stripeCustomerId,
                invoice.getId(),
                tenantId
        );

        // Save payment (PENDING initially)
        Payment payment = paymentRepository.save(
                Payment.builder()
                        .invoice(invoice)
                        .tenantId(tenantId)
                        .amount(invoice.getAmount())
                        .stripePaymentIntentId(intent.getId())
                        .status(PaymentStatus.PENDING)
                        .build()
        );


        payment.setStatus(PaymentStatus.SUCCESS);
        invoice.setStatus(InvoiceStatus.PAID);

        paymentRepository.save(payment);
        invoiceRepository.save(invoice);

        //  Return response
        return new PaymentResponse(
                invoice.getId(),
                payment.getStatus(),
                intent.getId(),
                payment.getAmount()
        );
    }
}
