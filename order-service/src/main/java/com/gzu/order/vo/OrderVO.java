package com.gzu.order.vo;

import com.gzu.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderVO(Long id, Long userId, String productCode, Integer quantity, BigDecimal amount, OrderStatus status,
                      LocalDateTime createdAt) {
}

