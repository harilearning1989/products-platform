package com.web.notification.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "notifications",
        indexes = {
                @Index(name = "idx_notification_event_id", columnList = "eventId"),
                @Index(name = "idx_notification_status", columnList = "status")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For idempotency
    @Column(nullable = false, unique = true)
    private String eventId;

    @Column(nullable = false)
    private String type; // ORDER_CREATED, PAYMENT_SUCCESS

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String channel; // EMAIL, SMS

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private String status; // PENDING, SENT, FAILED

    private int retryCount;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant sentAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.status = "PENDING";
        this.retryCount = 0;
    }
}
