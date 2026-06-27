package com.gzu.order.feign;

import com.gzu.common.core.result.ApiResponse;
import com.gzu.order.feign.dto.DeductStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "stock-service", path = "/internal/stock", fallback = com.gzu.order.feign.fallback.StockFeignClientFallback.class)
public interface StockFeignClient {

    @PostMapping("/deduct")
    ApiResponse<Void> deduct(@RequestBody DeductStockRequest request);
}
