package com.gzu.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class DeductStockRequest {
    @NotBlank(message = "productCode is required")
    private String productCode;

    @Min(value = 1, message = "quantity must be greater than 0")
    private Integer quantity;

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
}
