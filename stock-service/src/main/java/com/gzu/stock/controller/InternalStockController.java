package com.gzu.stock.controller;

import com.gzu.common.core.result.ApiResponse;
import com.gzu.stock.dto.DeductStockRequest;
import com.gzu.stock.service.StockService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/stock")
public class InternalStockController {
    private final StockService stockService;

    public InternalStockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/deduct")
    public ApiResponse<Void> deduct(@Valid @RequestBody DeductStockRequest request) {
        stockService.deduct(request);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{productCode}")
    public ApiResponse<Integer> query(@PathVariable("productCode") String productCode) {
        return ApiResponse.ok(stockService.query(productCode));
    }
}
