package com.web.notification.services;

import com.web.notification.dtos.OrderCreatedEvent;
import com.web.notification.models.Notification;
import com.web.notification.repos.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final EmailService emailService;

    @Override
    public void processOrderNotification(OrderCreatedEvent event) {

        // Idempotency check
        if (repository.findByEventId(event.eventId()).isPresent()) {
            log.info("Notification already processed for event {}", event.eventId());
            return;
        }

        String content = buildEmailContent(event);

        Notification notification = Notification.builder()
                .eventId(event.eventId())
                .type("ORDER_CREATED")
                .recipient(event.customerEmail())
                .channel("EMAIL")
                .content(content)
                .build();

        repository.save(notification);

        try {
            emailService.sendEmail(
                    event.customerEmail(),
                    "Order Confirmation",
                    content
            );

            notification.setStatus("SENT");
            notification.setSentAt(java.time.Instant.now());

        } catch (Exception ex) {

            notification.setStatus("FAILED");
            notification.setRetryCount(notification.getRetryCount() + 1);

            log.error("Failed to send notification for event {}", event.eventId());
        }
    }

    private String buildEmailContent(OrderCreatedEvent event) {

        return """
                Dear Customer,
                
                Your order #%d has been successfully created.
                Total Amount: ₹%s
                
                Thank you for shopping with us!
                """.formatted(event.orderId(), event.amount());
    }
}
