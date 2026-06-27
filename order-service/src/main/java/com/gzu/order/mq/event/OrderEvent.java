package com.gzu.order.mq.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eventType;
    private Long orderId;
    private Long userId;
    private String productCode;
    private Integer quantity;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public OrderEvent() {
    }

    public OrderEvent(String eventType, Long orderId, Long userId, String productCode, 
                      Integer quantity, BigDecimal amount, LocalDateTime createdAt) {
        this.eventType = eventType;
        this.orderId = orderId;
        this.userId = userId;
        this.productCode = productCode;
        this.quantity = quantity;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
