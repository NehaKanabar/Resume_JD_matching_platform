package com.resume.resumematching.invoice;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.invoice.entity.Invoice;
import com.resume.resumematching.enums.InvoiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillingAccessValidator {

    private final InvoiceRepository invoiceRepository;

    /**
     * Ensures tenant has paid invoice before allowing usage
     */
    public void validateUploadAccess() {

        Long tenantId = TenantContext.getTenantId();

        Invoice latestInvoice = invoiceRepository
                .findTopByTenantIdOrderByCreatedAtDesc(tenantId)
                .orElseThrow(() ->
                        new RuntimeException("No invoice found. Please subscribe to a plan.")
                );

        if (latestInvoice.getStatus() != InvoiceStatus.PAID) {
            throw new RuntimeException(
                    "Uploads blocked. Please complete payment for your subscription."
            );
        }
    }
}

