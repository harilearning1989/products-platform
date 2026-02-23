package com.web.order.models;

import com.web.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders",
        indexes = {
                @Index(name = "idx_order_customer", columnList = "customerEmail"),
                @Index(name = "idx_order_status", columnList = "status")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerEmail;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;

    private Instant createdAt;

    private Instant updatedAt;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> items;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        status = OrderStatus.CREATED;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
