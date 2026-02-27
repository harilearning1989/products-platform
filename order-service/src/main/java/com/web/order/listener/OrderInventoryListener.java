package com.web.order.listener;

import com.web.order.dtos.InventoryStatusEvent;
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

    @KafkaListener(
            topics = "inventory-reserved",
            groupId = "order-group"
    )
    @Transactional
    public void handleInventoryReserved(String inventoryReservedEventJson) {
        System.out.println("OrderInventoryListener handleInventoryReserved");
        InventoryStatusEvent inventoryStatusEvent = objectMapper.readValue(inventoryReservedEventJson, InventoryStatusEvent.class);
        OrderProduct order = orderProductRepository
                .findById(inventoryStatusEvent.orderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: "
                                        + inventoryStatusEvent.orderId()));

        // Only update if still pending
        if (order.getStatus() == OrderStatus.PENDING) {

            order.setStatus(
                    OrderStatus.INVENTORY_RESERVED);

            // No need to call save()
            // JPA dirty checking will update
        }
    }

    // 🔹 FAILURE CASE
    @KafkaListener(topics = "inventory-failed", groupId = "order-group")
    @Transactional
    public void handleInventoryFailed(String inventoryReservedEventJson) {
        System.out.println("OrderInventoryListener handleInventoryFailed");
        InventoryStatusEvent inventoryStatusEvent = objectMapper.readValue(inventoryReservedEventJson, InventoryStatusEvent.class);
        OrderProduct order = orderProductRepository
                .findById(inventoryStatusEvent.orderId())
                .orElseThrow();

        order.setStatus(OrderStatus.CANCELLED);
    }
}
