package com.resume.resumematching.payment;

import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> payInvoice(
            @PathVariable Long invoiceId
    ) throws Exception {

        PaymentResponse paymentResponse =
                paymentService.payInvoice(invoiceId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Payment processed successfully",
                        paymentResponse
                )
        );
    }
}
