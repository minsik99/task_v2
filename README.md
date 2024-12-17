# 재입고 알림 시스

## 프로젝트 개요
상품이 재입고 되었을 때, 재입고 알림을 설정한 유저들에게 **재입고 알림**을 보내줍니다.

---

## **기술 스택**
- **Backend**: Java 17, Spring Boot 3.4.0
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle
- **Containerization**: Docker, Docker Compose

---

## **ERD**
아래는 프로젝트의 ERD(Entity Relationship Diagram)입니다:

**Product (상품)**

id (PK)
name
restock_cycle
stock_status (boolean)

**ProductNotificationHistory (알림 상태 기록)**

id (PK)
product_id (FK)
restock_cycle
status (IN_PROGRESS, CANCELED_BY_SOLD_OUT, COMPLETED)
last_user_id
created_at
updated_at

**ProductUserNotification (알림 구독 사용자)**

id (PK)
product_id (FK)
user_id
is_active
created_at
updated_at

**ProductUserNotificationHistory (알림 전송 히스토리)**

id (PK)
product_id (FK)
user_id
restock_cycle
sent_at

---

## **기능 설계**

### **핵심 기능**
1. **재입고 알림 전송**  
   - 상품이 재입고되면 알림을 설정한 사용자에게 순차적으로 알림 전송.
   - 1초에 최대 500명에게 알림 전송.

2. **재고 소진 시 알림 중단**  
   - 재고가 소진되면 알림을 중단하고 마지막 알림 사용자 ID를 기록.

3. **재시도 API**  
   - 중단된 알림은 마지막 사용자 이후부터 재시도 가능.

4. **알림 상태 관리**  
   - `IN_PROGRESS`: 알림 발송 중  
   - `CANCELED_BY_SOLD_OUT`: 재고 소진으로 중단  
   - `COMPLETED`: 알림 발송 완료  

---

## **API 명세**

### 1. **재입고 알림 전송 API**
- **Endpoint**: `POST /products/{productId}/notifications/re-stock`  
- **설명**: 자동 재입고 알림 전송  

### 2. **수동 재입고 알림 전송 API**
- **Endpoint**: `POST /admin/products/{productId}/notifications/re-stock`  
- **설명**: 관리자에 의한 수동 알림 전송  

---

## **로직 흐름**

1. **상품 조회 및 재입고 처리**  
   - `Product` 엔티티의 `restockCycle`을 증가시킵니다.  

2. **알림 상태 초기화**  
   - `ProductNotificationHistory`에 상태를 `IN_PROGRESS`로 기록합니다.  

3. **알림 대상 사용자 조회**  
   - `ProductUserNotification` 테이블에서 알림을 설정한 사용자 목록을 가져옵니다.  
   - `lastUserId` 이후 사용자부터 알림을 전송합니다.  

4. **재고 확인 및 중단**  
   - 알림 전송 중 **재고 상태**를 확인합니다.  
   - 재고가 소진되면 알림을 중단하고 상태를 `CANCELED_BY_SOLD_OUT`으로 기록합니다.  

5. **알림 히스토리 기록**  
   - 성공적으로 알림을 전송한 사용자 정보를 `ProductUserNotificationHistory`에 기록합니다.

6. **알림 상태 최종 업데이트**  
   - 알림 전송이 모두 완료되면 상태를 `COMPLETED`로 기록합니다.  

---

## **테스트 및 검증**

### **단위 테스트**
- **Controller 테스트**:  
   - 알림 전송 API와 관리자 수동 API의 요청 및 응답 테스트  
- **Service 테스트**:  
   - 비즈니스 로직 검증 (재고 소진 시 알림 중단, 마지막 사용자 ID 저장)  
