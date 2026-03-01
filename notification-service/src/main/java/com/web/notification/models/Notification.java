package com.web.notification.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notifications_order_id", columnList = "order_id"),
                @Index(name = "idx_notifications_status", columnList = "status"),
                @Index(name = "idx_notifications_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 50)
    private String type; // PAYMENT_SUCCESS / PAYMENT_FAILED

    @Column(nullable = false, length = 30)
    private String status; // PENDING / SENT / FAILED

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }
}
