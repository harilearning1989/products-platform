package com.web.notification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.notification.dtos.PaymentFailedEvent;
import com.web.notification.dtos.PaymentSuccessEvent;
import com.web.notification.models.Notification;
import com.web.notification.repos.NotificationRepository;
import com.web.notification.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final NotificationRepository repository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    /*
     * 1️⃣ Payment Success Notification
     */
    @KafkaListener(
            topics = "payment-success",
            groupId = "notification-group"
    )
    @Transactional
    public void handlePaymentSuccess(String json) throws Exception {

        PaymentSuccessEvent event =
                objectMapper.readValue(
                        json,
                        PaymentSuccessEvent.class);

        Notification notification = Notification.builder()
                .orderId(event.orderId())
                .email(event.customerEmail())
                .type("PAYMENT_SUCCESS")
                .status("PENDING")
                .build();

        repository.save(notification);

        try {

            String content =
                    "Your order " + event.orderId() +
                            " payment was successful.\n" +
                            "Transaction ID: " + event.transactionId();

            emailService.sendEmail(
                    event.customerEmail(),
                    "Payment Successful",
                    content
            );

            notification.setStatus("SENT");
            notification.setSentAt(Instant.now());

        } catch (Exception ex) {

            log.error("Email sending failed", ex);

            notification.setStatus("FAILED");
            notification.setFailureReason(ex.getMessage());
        }
    }

    /*
     * 2️⃣ Payment Failed Notification
     */
    @KafkaListener(
            topics = "payment-failed",
            groupId = "notification-group"
    )
    @Transactional
    public void handlePaymentFailed(String json) throws Exception {
        PaymentFailedEvent event = objectMapper.readValue(json, PaymentFailedEvent.class);

        Notification notification = Notification.builder()
                .orderId(event.orderId())
                .email(event.customerEmail())
                .type("PAYMENT_FAILED")
                .status("PENDING")
                .build();

        repository.save(notification);

        try {
            String content = "Your order " + event.orderId() + " payment failed.Reason: " + event.failureReason();

            emailService.sendEmail(event.customerEmail(), "Payment Failed", content);

            notification.setStatus("SENT");
            notification.setSentAt(Instant.now());

        } catch (Exception ex) {
            log.error("Email sending failed", ex);

            notification.setStatus("FAILED");
            notification.setFailureReason(ex.getMessage());
        }
    }
}
