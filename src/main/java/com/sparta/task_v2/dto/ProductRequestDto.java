package com.sparta.task_v2.dto;

import lombok.Getter;

@Getter
public class ProductRequestDto {
    private String name;
    private Integer restockCycle;
    private Boolean stockStatus;

    public ProductRequestDto(String name, Integer restockCycle, Boolean stockStatus) {
        this.name = name;
        this.restockCycle = restockCycle;
        this.stockStatus = stockStatus;
    }
}
