package com.sparta.task_v2.dto;

import lombok.Getter;

@Getter
public class ProductNotificationResponseDto {
    private final Long productId;
    private final Integer restockCycle;
    private final String status;
    private final Long lastUserId;
    private final String updatedAt;

    public ProductNotificationResponseDto(Long productId, Integer restockCycle, String status, Long lastUserId, String updatedAt) {
        this.productId = productId;
        this.restockCycle = restockCycle;
        this.status = status;
        this.lastUserId = lastUserId;
        this.updatedAt = updatedAt;
    }
}
