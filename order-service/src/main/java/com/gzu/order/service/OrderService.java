package com.gzu.order.service;

import com.gzu.order.dto.CreateOrderRequest;
import com.gzu.order.vo.OrderVO;

import java.util.List;

public interface OrderService {
    OrderVO create(Long userId, CreateOrderRequest request);

    List<OrderVO> listByUser(Long userId);
}

