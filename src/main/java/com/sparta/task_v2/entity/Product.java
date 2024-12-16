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

    public Product(String name, Integer restockCycle, Boolean stockStatus) {
        this.name = name;
        this.restockCycle = restockCycle != null ? restockCycle : 0;
        this.stockStatus = stockStatus != null ? stockStatus : true;
    }

    public void restock(int newRestockCycle) {
        this.restockCycle = newRestockCycle;
        this.stockStatus = true;
    }

    public void markOutOfStock() {
        this.stockStatus = false;
    }

    public void incrementRestockCycle() {
        this.restockCycle += 1;
        this.stockStatus = true;
    }
}