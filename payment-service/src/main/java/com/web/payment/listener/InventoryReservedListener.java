package com.web.payment.listener;

import com.web.payment.produce.PaymentEventProduce;
import com.web.payment.repos.PaymentRepository;
import com.web.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class InventoryReservedListener {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final PaymentEventProduce paymentEventProduce;
    private final PaymentService paymentService;


    @KafkaListener(
            topics = "inventory-reserved",
            groupId = "payment-group"
    )
    public void handleInventoryReserved(String inventoryReservedEventJson) {
        paymentService.processInventoryReserved(inventoryReservedEventJson);
    }

    /*
    @KafkaListener(
            topics = "inventory-reserved",
            groupId = "payment-group"
    )
    @Transactional
    public void handleInventoryReserved(String inventoryReservedEventJson) {
        System.out.println("Payment service OrderInventoryListener handleInventoryReserved");
        OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(inventoryReservedEventJson, OrderCreatedEvent.class);
        // Avoid duplicate processing
        if (paymentRepository.findByOrderId(orderCreatedEvent.orderId()).isPresent()) {
            return;
        }

        // Create payment record
        Payment payment = Payment.builder()
                .orderId(orderCreatedEvent.orderId())
                .customerEmail(orderCreatedEvent.customerEmail())
                .amount(orderCreatedEvent.totalAmount())
                .status(PaymentStatus.INITIATED)
                .build();

        paymentRepository.save(payment);

        // Simulate payment processing
        boolean paymentSuccess = processPayment(orderCreatedEvent.totalAmount());

        if (paymentSuccess) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(UUID.randomUUID().toString());

            publishPaymentSuccess(payment);

        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Payment gateway error");

            publishPaymentFailed(payment,orderCreatedEvent.items());
        }
    }*/

    /*
     * Simulated payment gateway
     */
   /* private boolean processPayment(BigDecimal amount) {
        return true; // simulate success
    }

    private void publishPaymentSuccess(Payment payment) {
        PaymentSuccessEvent paymentEvent =
                new PaymentSuccessEvent(
                        UUID.randomUUID().toString(),
                        payment.getOrderId(),
                        payment.getAmount(),
                        payment.getTransactionId(),
                        Instant.now(),
                        null
                );

        paymentEventProduce.sendPaymentSuccessEvent(payment.getOrderId().toString(), paymentEvent);
    }

    private void publishPaymentFailed(Payment payment, List<OrderItemEvent> items) {
        PaymentFailedEvent paymentFailedEvent =
                new PaymentFailedEvent(
                        UUID.randomUUID().toString(),
                        payment.getOrderId(),
                        payment.getAmount(),
                        payment.getTransactionId(),
                        Instant.now(),
                        payment.getFailureReason(),
                        items
                );

        paymentEventProduce.sendPaymentFailureEvent(payment.getOrderId().toString(), paymentFailedEvent);
    }*/

   /* @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvents(Payment payment) {
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            publishPaymentSuccess(payment);
        } else {
            publishPaymentFailed(payment);
        }
    }*/
}
