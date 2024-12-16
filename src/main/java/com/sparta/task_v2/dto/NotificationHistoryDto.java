package com.sparta.task_v2.dto;

import lombok.Getter;

@Getter
public class NotificationHistoryDto {
    private final Long productId;
    private final Long userId;
    private final Integer restockCycle;
    private final String sentAt;

    public NotificationHistoryDto(Long productId, Long userId, Integer restockCycle, String sentAt) {
        this.productId = productId;
        this.userId = userId;
        this.restockCycle = restockCycle;
        this.sentAt = sentAt;
    }
}
