package com.web.order.wrapper;

import com.web.order.client.PaymentClient;
import com.web.order.dtos.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentClientWrapper {

    private final PaymentClient paymentClient;

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        return paymentClient.getPaymentByOrderId(orderId);
    }

    public PaymentResponse paymentFallback(Long orderId, Throwable ex) {
        log.error("Payment service is DOWN. Circuit breaker triggered.", ex);

        throw new RuntimeException("Product service unavailable. Try later.");
    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public List<PaymentResponse> getPaymentsBulk(List<Long> orderIds) {
        return paymentClient.fetchAllPayments(orderIds);
    }

}
