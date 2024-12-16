package com.sparta.task_v2.repository;

import com.sparta.task_v2.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    List<ProductUserNotification> findByProductIdAndIsActive(Long productId, Boolean isActive);
}
