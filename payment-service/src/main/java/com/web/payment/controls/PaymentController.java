package com.web.payment.controls;

import com.web.payment.dtos.PaymentRequest;
import com.web.payment.dtos.PaymentResponse;
import com.web.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public PaymentResponse process(@RequestBody PaymentRequest request) {
        return service.processPayment(request);
    }
}
