package com.sparta.task_v2.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProductUserNotificationHistory")
@Getter
public class ProductUserNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "restock_cycle", nullable = false)
    private Integer restockCycle;

    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    protected ProductUserNotificationHistory() {
    }

    public ProductUserNotificationHistory(Product product, Long userId, Integer restockCycle) {
        this.product = product;
        this.userId = userId;
        this.restockCycle = restockCycle;
    }
}
