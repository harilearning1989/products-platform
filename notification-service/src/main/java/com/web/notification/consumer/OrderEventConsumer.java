package com.web.notification.consumer;

import com.web.notification.dtos.OrderCreatedEvent;
import com.web.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-created", groupId = "notification-group")
    public void consume(OrderCreatedEvent event) {
        notificationService.processOrderNotification(event);
    }
}
