package com.web.order.wrapper;

import com.product.dtos.PaymentResponse;
import com.web.order.client.PaymentClient;
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

    @CircuitBreaker(name = "paymentService", fallbackMethod = "getBulkPaymentFallback")
    public List<PaymentResponse> getPaymentsBulk(List<Long> orderIds) {
        return paymentClient.fetchAllPayments(orderIds);
    }

    public PaymentResponse paymentFallback(Long orderId, Throwable ex) {
        log.error("Payment service is DOWN. Circuit breaker triggered.", ex);

        throw new RuntimeException("Product service unavailable. Try later." + orderId);
    }

    public List<PaymentResponse> getBulkPaymentFallback(List<Long> orderIds, Throwable ex) {
        log.error("Payment service DOWN for bulk orders {}", orderIds, ex);

        return orderIds.stream()
                .map(PaymentResponse::empty)
                .toList();
    }

}
