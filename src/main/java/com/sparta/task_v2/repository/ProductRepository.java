package com.sparta.task_v2.repository;

import com.sparta.task_v2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
