package com.web.history.controls;

import com.web.history.dtos.PaymentHistoryResponse;
import com.web.history.services.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-history")
@RequiredArgsConstructor
public class PaymentHistoryController {

    private final PaymentHistoryService service;

    @GetMapping("/customer/{email}")
    public List<PaymentHistoryResponse> getByCustomer(
            @PathVariable String email) {
        return service.getByCustomer(email);
    }

    @GetMapping("/order/{orderId}")
    public List<PaymentHistoryResponse> getByOrder(
            @PathVariable Long orderId) {
        return service.getByOrder(orderId);
    }

    @GetMapping("/status/{status}")
    public List<PaymentHistoryResponse> getByStatus(
            @PathVariable String status) {
        return service.getByStatus(status);
    }
}
