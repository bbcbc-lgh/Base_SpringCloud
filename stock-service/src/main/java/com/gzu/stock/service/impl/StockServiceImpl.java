package com.gzu.stock.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzu.common.core.exception.BusinessException;
import com.gzu.common.core.exception.ErrorCode;
import com.gzu.stock.dto.DeductStockRequest;
import com.gzu.stock.entity.ProductStock;
import com.gzu.stock.mapper.ProductStockMapper;
import com.gzu.stock.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl implements StockService {
    private final ProductStockMapper productStockMapper;

    public StockServiceImpl(ProductStockMapper productStockMapper) {
        this.productStockMapper = productStockMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(DeductStockRequest request) {
        int updated = productStockMapper.deduct(request.getProductCode(), request.getQuantity());
        if (updated == 0) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR,
                    "insufficient stock for product: " + request.getProductCode());
        }
    }

    @Override
    public Integer query(String productCode) {
        ProductStock stock = productStockMapper.selectOne(
                Wrappers.<ProductStock>lambdaQuery().eq(ProductStock::getProductCode, productCode));
        if (stock == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "product not found: " + productCode);
        }
        return stock.getStock();
    }
}
