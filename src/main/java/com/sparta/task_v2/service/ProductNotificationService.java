package com.sparta.task_v2.service;

import com.sparta.task_v2.dto.ProductNotificationResponseDto;
import com.sparta.task_v2.entity.*;
import com.sparta.task_v2.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductNotificationService {

    private final ProductRepository productRepository;
    private final ProductUserNotificationRepository userNotificationRepository;
    private final ProductNotificationHistoryRepository notificationHistoryRepository;
    private final ProductUserNotificationHistoryRepository userNotificationHistoryRepository;

    @Transactional
    public ProductNotificationResponseDto sendRestockNotification(Long productId, boolean isManual) {
        // 1. 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        // 2. 상품 재입고 처리
        product.incrementRestockCycle();
        productRepository.save(product);

        // 3. 재입고 알림 상태 초기화
        ProductNotificationHistory notificationHistory = new ProductNotificationHistory(
                product,
                product.getRestockCycle(),
                ProductNotificationHistory.NotificationStatus.IN_PROGRESS
        );
        notificationHistoryRepository.save(notificationHistory);

        // 4. 알림 전송 대상 조회
        List<ProductUserNotification> notifications = userNotificationRepository.findByProductIdAndIsActive(productId, true);

        // 5. 알림 전송 루프
        int sentCount = 0;
        for (ProductUserNotification userNotification : notifications) {
            if (sentCount >= 500) {
                break;
            }

            // 알림 전송 처리
            ProductUserNotificationHistory history = new ProductUserNotificationHistory(
                    product,
                    userNotification.getUserId(),
                    product.getRestockCycle()
            );
            userNotificationHistoryRepository.save(history);
            sentCount++;
        }

        // 6. 알림 상태 업데이트
        if (notifications.size() > 0) {
            notificationHistory.updateStatus(
                    ProductNotificationHistory.NotificationStatus.COMPLETED,
                    notifications.get(notifications.size() - 1).getUserId()
            );
        } else {
            notificationHistory.updateStatus(ProductNotificationHistory.NotificationStatus.COMPLETED, null);
        }

        notificationHistoryRepository.save(notificationHistory);

        // 7. 응답 생성
        return new ProductNotificationResponseDto(
                product.getId(),
                product.getRestockCycle(),
                notificationHistory.getStatus().name(),
                notificationHistory.getLastUserId(),
                notificationHistory.getUpdatedAt().toString()
        );
    }
}
