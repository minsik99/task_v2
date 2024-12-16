package com.sparta.task_v2.repository;

import com.sparta.task_v2.entity.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {
    Optional<ProductNotificationHistory> findByProductIdAndRestockCycle(Long productId, Integer restockCycle);
}
