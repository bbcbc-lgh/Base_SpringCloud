package com.gzu.order.feign.fallback;

import com.gzu.common.core.exception.ErrorCode;
import com.gzu.common.core.result.ApiResponse;
import com.gzu.order.feign.StockFeignClient;
import com.gzu.order.feign.dto.DeductStockRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StockFeignClientFallback implements StockFeignClient {
    private static final Logger logger = LoggerFactory.getLogger(StockFeignClientFallback.class);

    @Override
    public ApiResponse<Void> deduct(DeductStockRequest request) {
        logger.warn("stock-service fallback triggered for deduct, productCode: {}", request.getProductCode());
        return ApiResponse.fail(ErrorCode.SERVICE_UNAVAILABLE, "stock service temporarily unavailable");
    }
}
