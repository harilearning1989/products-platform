package com.web.order.listener;

import com.web.order.dtos.InventoryStatusEvent;
import com.web.order.enums.OrderStatus;
import com.web.order.models.OrderProduct;
import com.web.order.repos.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderInventoryListener {

    private final OrderProductRepository orderProductRepository;

    @KafkaListener(
            topics = "inventory-reserved",
            groupId = "order-group"
    )
    @Transactional
    public void handleInventoryReserved(
            InventoryStatusEvent event) {

        OrderProduct order = orderProductRepository
                .findById(event.orderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: "
                                        + event.orderId()));

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
    public void handleInventoryFailed(InventoryStatusEvent event) {
        OrderProduct order = orderProductRepository
                .findById(event.orderId())
                .orElseThrow();

        order.setStatus(OrderStatus.CANCELLED);
    }
}
