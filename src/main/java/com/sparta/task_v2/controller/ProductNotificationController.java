package com.sparta.task_v2.controller;

import com.sparta.task_v2.dto.ProductNotificationResponseDto;
import com.sparta.task_v2.service.ProductNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductNotificationController {

    private final ProductNotificationService productNotificationService;

    // 재입고 알림 전송 API
    @PostMapping("/products/{productId}/notifications/re-stock")
    public ResponseEntity<ProductNotificationResponseDto> sendRestockNotification(@PathVariable Long productId) {
        ProductNotificationResponseDto response = productNotificationService.sendRestockNotification(productId, false);
        return ResponseEntity.ok(response);
    }

    // 재입고 알림 전송 API (manual)
    @PostMapping("/admin/products/{productId}/notifications/re-stock")
    public ResponseEntity<ProductNotificationResponseDto> manualRestockNotification(@PathVariable Long productId) {
        ProductNotificationResponseDto response = productNotificationService.sendRestockNotification(productId, true);
        return ResponseEntity.ok(response);
    }
}
