package com.gzu.stock.controller;

import com.gzu.common.core.result.ApiResponse;
import com.gzu.stock.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{productCode}")
    public ApiResponse<Integer> query(@PathVariable("productCode") String productCode) {
        return ApiResponse.ok(stockService.query(productCode));
    }
}
