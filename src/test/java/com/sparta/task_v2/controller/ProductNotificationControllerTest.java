package com.sparta.task_v2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.task_v2.dto.ProductNotificationResponseDto;
import com.sparta.task_v2.service.ProductNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductNotificationController.class)
class ProductNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductNotificationService productNotificationService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Mock Service 반환값 설정
        Mockito.when(productNotificationService.sendRestockNotification(anyLong(), eq(false)))
                .thenReturn(new ProductNotificationResponseDto(1L, 2, "COMPLETED", 10L, "2024-12-16T10:00:00"));

        Mockito.when(productNotificationService.sendRestockNotification(anyLong(), eq(true)))
                .thenReturn(new ProductNotificationResponseDto(1L, 3, "COMPLETED", 12L, "2024-12-16T10:05:00"));
    }

    @Test
    @DisplayName("재입고 알림 전송 API 테스트")
    void sendRestockNotification() throws Exception {
        mockMvc.perform(post("/products/1/notifications/re-stock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.restockCycle").value(2))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.lastUserId").value(10L))
                .andExpect(jsonPath("$.updatedAt").value("2024-12-16T10:00:00"));
    }

    @Test
    @DisplayName("재입고 알림 전송 API (manual) 테스트")
    void manualRestockNotification() throws Exception {
        mockMvc.perform(post("/admin/products/1/notifications/re-stock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.restockCycle").value(3))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.lastUserId").value(12L))
                .andExpect(jsonPath("$.updatedAt").value("2024-12-16T10:05:00"));
    }
}
