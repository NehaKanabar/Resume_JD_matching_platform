package com.resume.resumematching.payment;


import com.resume.resumematching.payment.entity.Invoice;
import com.resume.resumematching.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByTenantId(Long tenantId);

    List<Invoice> findByTenantIdAndStatus(Long tenantId, InvoiceStatus status);

    Optional<Invoice> findTopByTenantIdOrderByCreatedAtDesc(Long tenantId);

}

