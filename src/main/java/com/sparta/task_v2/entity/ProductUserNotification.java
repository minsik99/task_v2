package com.sparta.task_v2.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "ProductUserNotification")
@Getter
public class ProductUserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    protected ProductUserNotification() {
    }

    public ProductUserNotification(Product product, Long userId) {
        this.product = product;
        this.userId = userId;
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
}
