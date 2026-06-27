package com.gzu.stock.service;

import com.gzu.stock.dto.DeductStockRequest;

public interface StockService {
    void deduct(DeductStockRequest request);

    Integer query(String productCode);
}
