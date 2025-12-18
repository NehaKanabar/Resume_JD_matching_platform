package com.resume.resumematching.repository;


import com.resume.resumematching.entity.Invoice;
import com.resume.resumematching.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByTenantId(Long tenantId);

    List<Invoice> findByTenantIdAndStatus(Long tenantId, InvoiceStatus status);
}

