package com.web.order.producer;

import com.web.order.dtos.OrderCreatedEvent;
import com.web.order.dtos.OrderItemEvent;
import com.web.order.dtos.OrderItemRequest;
import com.web.order.models.OrderProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderPublish {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreated(
            OrderProduct order,
            List<OrderItemRequest> items) {

        OrderCreatedEvent event =
                new OrderCreatedEvent(
                        UUID.randomUUID().toString(),
                        order.getId(),
                        order.getCustomerEmail(),
                        order.getTotalAmount(),
                        items.stream()
                                .map(i -> new OrderItemEvent(
                                        i.productId(),
                                        i.quantity()))
                                .toList(),
                        Instant.now()
                );

        kafkaTemplate.send("order-created",
                order.getId().toString(),
                event);
    }
}
