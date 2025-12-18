package com.resume.resumematching.controller;

import com.resume.resumematching.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{invoiceId}")
    public ResponseEntity<?> payInvoice(@PathVariable Long invoiceId) throws Exception {
        return ResponseEntity.ok(paymentService.payInvoice(invoiceId));
    }
}

