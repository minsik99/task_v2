package com.sparta.task_v2.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Product")
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "restock_cycle", nullable = false)
    private Integer restockCycle = 0;

    @Column(name = "stock_status", nullable = false)
    private Boolean stockStatus = true;

    protected Product() {
    }

    public void increaseRestockAndStatus() {
        this.restockCycle += 1;
        this.stockStatus = true;
    }
}