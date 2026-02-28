package com.web.payment.produce;

import com.web.payment.dtos.PaymentFailedEvent;
import com.web.payment.dtos.PaymentProcessedInternalEvent;
import com.web.payment.dtos.PaymentSuccessEvent;
import com.web.payment.enums.PaymentStatus;
import com.web.payment.models.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventHandler {

    private final PaymentEventProduce paymentEventProduce;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentProcessed(PaymentProcessedInternalEvent event) {
        Payment payment = event.payment();

        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            PaymentSuccessEvent successEvent =
                    new PaymentSuccessEvent(
                            UUID.randomUUID().toString(),
                            payment.getOrderId(),
                            payment.getAmount(),
                            payment.getTransactionId(),
                            Instant.now(),
                            null
                    );

            paymentEventProduce.sendPaymentSuccessEvent(
                    payment.getOrderId().toString(),
                    successEvent
            );

        } else {

            PaymentFailedEvent failedEvent =
                    new PaymentFailedEvent(
                            UUID.randomUUID().toString(),
                            payment.getOrderId(),
                            payment.getAmount(),
                            payment.getTransactionId(),
                            Instant.now(),
                            payment.getFailureReason(),
                            event.items()
                    );

            paymentEventProduce.sendPaymentFailureEvent(
                    payment.getOrderId().toString(),
                    failedEvent
            );
        }
    }

        /*@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        public void handleSuccess(PaymentSucceededInternalEvent event) {

            Payment payment = event.payment();

            PaymentSuccessEvent successEvent =
                    new PaymentSuccessEvent(
                            UUID.randomUUID().toString(),
                            payment.getOrderId(),
                            payment.getAmount(),
                            payment.getTransactionId(),
                            Instant.now(),
                            null
                    );

            paymentEventProduce.sendPaymentSuccessEvent(
                    payment.getOrderId().toString(),
                    successEvent
            );
        }

        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        public void handleFailure(PaymentFailedInternalEvent event) {

            Payment payment = event.payment();

            PaymentFailedEvent failedEvent =
                    new PaymentFailedEvent(
                            UUID.randomUUID().toString(),
                            payment.getOrderId(),
                            payment.getAmount(),
                            payment.getTransactionId(),
                            Instant.now(),
                            payment.getFailureReason(),
                            event.items()
                    );

            paymentEventProduce.sendPaymentFailureEvent(
                    payment.getOrderId().toString(),
                    failedEvent
            );
    }*/
}
