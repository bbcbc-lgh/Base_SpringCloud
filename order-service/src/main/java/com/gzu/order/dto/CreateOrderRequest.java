package com.gzu.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class CreateOrderRequest {
    @NotBlank(message = "productCode is required")
    private String productCode;

    @Min(value = 1, message = "quantity must be greater than 0")
    private Integer quantity;

    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

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
}

