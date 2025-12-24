package com.resume.resumematching.repository;

import com.resume.resumematching.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByTenantId(Long tenantId);

    List<Payment> findByInvoiceId(Long invoiceId);
}

