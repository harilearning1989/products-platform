package com.web.history.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment_history",
        indexes = {
                @Index(name = "idx_ph_order", columnList = "orderId"),
                @Index(name = "idx_ph_email", columnList = "customerEmail"),
                @Index(name = "idx_ph_status", columnList = "status")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long paymentId;
    private Long orderId;
    private String customerEmail;
    private BigDecimal amount;
    private String status;
    private String transactionId;

    private Instant eventTime;

    @PrePersist
    public void prePersist() {
        eventTime = Instant.now();
    }
}
