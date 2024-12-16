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

        if (!product.getStockStatus()) {
            throw new IllegalStateException("재고가 없는 상품입니다.");
        }

        // 2. 재입고 회차 증가
        if (isManual) {
            System.out.println("관리자에 의해 수동 재입고 알림이 요청되었습니다.");
        }
        product.increaseRestockAndStatus();
        productRepository.save(product);

        // 3. 알림 상태 초기화
        ProductNotificationHistory history = notificationHistoryRepository.findByProductIdAndRestockCycle(productId, product.getRestockCycle())
                .orElse(new ProductNotificationHistory(product, product.getRestockCycle(), ProductNotificationHistory.NotificationStatus.IN_PROGRESS));
        notificationHistoryRepository.save(history);

        // 4. 알림 대상 조회
        List<ProductUserNotification> notifications = userNotificationRepository.findByProductIdAndIsActive(productId, true);
        Long lastSentUserId = history.getLastUserId();

        int sentCount = 0;
        for (ProductUserNotification user : notifications) {
            // 마지막으로 전송된 사용자의 아이디 확인
            if (lastSentUserId != null && user.getUserId() <= lastSentUserId) {
                continue;
            }

            // 중간에 재고가 소진되면 알림 중단
            if (!product.getStockStatus()) {
                history.updateStatus(ProductNotificationHistory.NotificationStatus.CANCELED_BY_SOLD_OUT, lastSentUserId);
                notificationHistoryRepository.save(history);
                break;
            }

            System.out.println(user.getProduct() + "의 재고가 생겼습니다");

            // 히스토리 기록
            ProductUserNotificationHistory userHistory = new ProductUserNotificationHistory(
                    product, user.getUserId(), product.getRestockCycle());
            userNotificationHistoryRepository.save(userHistory);

            sentCount++;
            lastSentUserId = user.getUserId(); // 마지막 발송 사용자 ID 갱신
            history.updateStatus(ProductNotificationHistory.NotificationStatus.IN_PROGRESS, lastSentUserId);
            notificationHistoryRepository.save(history);

            if (sentCount >= 500) break;
        }

        // 5. 알림 상태 업데이트
        if (product.getStockStatus()) {
            history.updateStatus(ProductNotificationHistory.NotificationStatus.COMPLETED, lastSentUserId);
        }
        notificationHistoryRepository.save(history);

        // 6. 결과 반환
        return new ProductNotificationResponseDto(
                product.getId(), product.getRestockCycle(), history.getStatus().name(),
                history.getLastUserId(), history.getUpdatedAt().toString()
        );
    }
}
