package com.gzu.order.controller;

import com.gzu.common.core.exception.BusinessException;
import com.gzu.common.core.exception.ErrorCode;
import com.gzu.common.core.result.ApiResponse;
import com.gzu.common.core.security.TokenPayload;
import com.gzu.common.security.context.AuthContextHolder;
import com.gzu.order.dto.CreateOrderRequest;
import com.gzu.order.service.OrderService;
import com.gzu.order.vo.OrderVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 创建订单: 验证用户身份 → 调用OrderService → 触发SEATA全局事务和库存扣减
    // 入参: CreateOrderRequest包含产品代码、数量、金额，参数自动验证（@Valid）
    // 返回: OrderVO包含订单详情（ID、用户ID、产品代码、数量、金额、状态、创建时间）
    // 异常: 未认证返回401，订单创建失败返回1001，库存不足时触发全局事务回滚
    @PostMapping
    public ApiResponse<OrderVO> create(@Valid @RequestBody CreateOrderRequest request) {
        TokenPayload currentUser = AuthContextHolder.get();
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "missing user identity");
        }
        return ApiResponse.ok(orderService.create(currentUser.userId(), request));
    }

    // 获取我的订单列表: 验证用户身份 → 按创建时间倒序返回该用户的所有订单
    // 返回: OrderVO列表，按创建时间从新到旧排序
    // 异常: 未认证返回401
    @GetMapping("/my")
    public ApiResponse<List<OrderVO>> myOrders() {
        TokenPayload currentUser = AuthContextHolder.get();
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "missing user identity");
        }
        return ApiResponse.ok(orderService.listByUser(currentUser.userId()));
    }
}
