package com.sparta.task_v2.dto;

import lombok.Getter;

@Getter
public class UserNotificationRequestDto {
    private final Long productId;
    private final Long userId;

    public UserNotificationRequestDto(Long productId, Long userId) {
        this.productId = productId;
        this.userId = userId;
    }
}
