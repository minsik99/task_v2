package com.sparta.task_v2.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProductNotificationHistory")
@Getter
public class ProductNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "restock_cycle", nullable = false)
    private Integer restockCycle;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "last_user_id")
    private Long lastUserId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected ProductNotificationHistory() {
    }

    public ProductNotificationHistory(Product product, Integer restockCycle, NotificationStatus status) {
        this.product = product;
        this.restockCycle = restockCycle;
        this.status = status;
    }

    public void updateStatus(NotificationStatus status, Long lastUserId) {
        this.status = status;
        this.lastUserId = lastUserId;
        this.updatedAt = LocalDateTime.now();
    }

    public enum NotificationStatus {
        IN_PROGRESS,
        CANCELED_BY_SOLD_OUT,
        CANCELED_BY_ERROR,
        COMPLETED
    }
}
