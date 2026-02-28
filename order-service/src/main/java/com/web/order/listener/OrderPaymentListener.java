package com.web.order.listener;

import com.web.order.dtos.PaymentStatusEvent;
import com.web.order.enums.OrderStatus;
import com.web.order.models.OrderProduct;
import com.web.order.repos.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class OrderPaymentListener {

    private final OrderProductRepository orderProductRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-success", groupId = "order-group")
    @Transactional
    public void handlePaymentSuccess(String paymentSuccessEventJson) {
        System.out.println("OrderPaymentListener handlePaymentSuccess::"+paymentSuccessEventJson);
        PaymentStatusEvent paymentStatusEvent = objectMapper.readValue(paymentSuccessEventJson, PaymentStatusEvent.class);

        OrderProduct order = orderProductRepository.findById(paymentStatusEvent.orderId()).orElseThrow();

        order.setStatus(OrderStatus.CONFIRMED);
    }

    @KafkaListener(topics = "payment-failed", groupId = "order-group")
    @Transactional
    public void handlePaymentFailed(String paymentFailedEventJson) {
        System.out.println("OrderPaymentListener handlePaymentFailed::"+paymentFailedEventJson);
        PaymentStatusEvent paymentStatusEvent = objectMapper.readValue(paymentFailedEventJson, PaymentStatusEvent.class);

        OrderProduct order = orderProductRepository.findById(paymentStatusEvent.orderId()).orElseThrow();

        order.setStatus(OrderStatus.CANCELLED);
    }
}
