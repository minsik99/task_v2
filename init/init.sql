-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS secondTask;

-- 사용할 데이터베이스 선택
USE secondTask;

-- Product 테이블 생성
CREATE TABLE Product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    restock_cycle INT DEFAULT 0,
    stock_status BOOLEAN DEFAULT TRUE
);

-- ProductNotificationHistory 테이블 생성
CREATE TABLE ProductNotificationHistory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    restock_cycle INT NOT NULL,
    status ENUM('IN_PROGRESS', 'CANCELED_BY_SOLD_OUT', 'CANCELED_BY_ERROR', 'COMPLETED') NOT NULL,
    last_user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Product(id)
);

-- ProductUserNotification 테이블 생성
CREATE TABLE ProductUserNotification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (product_id) REFERENCES Product(id),
    INDEX idx_user_id (user_id)
);


-- ProductUserNotificationHistory 테이블 생성
CREATE TABLE ProductUserNotificationHistory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    restock_cycle INT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Product(id),
    FOREIGN KEY (user_id) REFERENCES ProductUserNotification(user_id)
);
