package com.web.order.listener;

import com.web.order.dtos.OrderCreatedEvent;
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
public class OrderInventoryListener {

    private final OrderProductRepository orderProductRepository;
    private final ObjectMapper objectMapper;

    // 🔹 FAILURE CASE
    @KafkaListener(topics = "inventory-failed", groupId = "order-group")
    @Transactional
    public void handleInventoryFailed(String inventoryReservedEventJson) {
        System.out.println("OrderInventoryListener handleInventoryFailed");
        OrderCreatedEvent inventoryStatusEvent = objectMapper.readValue(inventoryReservedEventJson, OrderCreatedEvent.class);
        OrderProduct order = orderProductRepository
                .findById(inventoryStatusEvent.orderId())
                .orElseThrow();

        order.setStatus(OrderStatus.CANCELLED);
    }
}
