package com.resume.resumematching.scheduler;

import com.resume.resumematching.entity.Invoice;
import com.resume.resumematching.entity.Subscription;
import com.resume.resumematching.enums.InvoiceStatus;
import com.resume.resumematching.enums.SubscriptionStatus;
import com.resume.resumematching.repository.InvoiceRepository;
import com.resume.resumematching.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;

    /**
     * Runs on 1st day of every month at 02:00 AM
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void generateMonthlyInvoices() {

        log.info("Starting monthly invoice generation");

        List<Subscription> activeSubscriptions =
                subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);

        for (Subscription sub : activeSubscriptions) {

            // Skip expired subscriptions
            if (sub.getEndDate().isBefore(LocalDate.now())) {
                sub.setStatus(SubscriptionStatus.EXPIRED);
                continue;
            }

            Invoice invoice = Invoice.builder()
                    .tenantId(sub.getTenant().getId())
                    .subscriptionId(sub.getId())
                    .amount(sub.getPlan().getPriceMonthly())
                    .status(InvoiceStatus.PENDING)
                    .dueDate(LocalDate.now().plusDays(10))
                    .createdAt(LocalDateTime.now())
                    .build();

            invoiceRepository.save(invoice);

            log.info("Invoice generated for tenant {}", sub.getTenant().getId());
        }

        log.info("Monthly invoice generation completed");
    }
}
