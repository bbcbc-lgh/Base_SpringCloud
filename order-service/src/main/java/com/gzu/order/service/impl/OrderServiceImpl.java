package com.gzu.order.service.impl;

import com.gzu.common.core.exception.BusinessException;
import com.gzu.common.core.exception.ErrorCode;
import com.gzu.common.core.result.ApiResponse;
import com.gzu.order.dto.CreateOrderRequest;
import com.gzu.order.entity.OrderEntity;
import com.gzu.order.enums.OrderStatus;
import com.gzu.order.feign.StockFeignClient;
import com.gzu.order.feign.dto.DeductStockRequest;
import com.gzu.order.mapper.OrderMapper;
import com.gzu.order.mq.event.OrderEvent;
import com.gzu.order.mq.producer.OrderEventProducer;
import com.gzu.order.service.OrderService;
import com.gzu.order.vo.OrderVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;
    private final StockFeignClient stockFeignClient;

    public OrderServiceImpl(OrderMapper orderMapper,
                            OrderEventProducer orderEventProducer,
                            StockFeignClient stockFeignClient) {
        this.orderMapper = orderMapper;
        this.orderEventProducer = orderEventProducer;
        this.stockFeignClient = stockFeignClient;
    }

    /**
     * 创建订单 - 使用SEATA分布式事务保证跨库一致性
     * 业务流程：
     * 1. 在order-service库中创建订单记录
     * 2. 通过OpenFeign调用stock-service扣减库存
     * 3. 若扣库存失败或超时 → 整个事务回滚（包括订单）
     * 4. 成功后发送ORDER_CREATED事件到消息队列
     *
     * @param userId 当前用户ID
     * @param request 订单请求，包含商品编码/数量/金额
     * @return 订单VO
     * @throws BusinessException 扣库存失败、stock-service不可用时抛出
     */
    @Override
    @GlobalTransactional(name = "create-order-tx", rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public OrderVO create(Long userId, CreateOrderRequest request) {
        // 插入订单记录到本地数据库，初始状态为CREATED
        OrderEntity entity = new OrderEntity();
        entity.setUserId(userId);
        entity.setProductCode(request.getProductCode());
        entity.setQuantity(request.getQuantity());
        entity.setAmount(request.getAmount());
        entity.setStatus(OrderStatus.CREATED);
        entity.setCreatedAt(LocalDateTime.now());
        orderMapper.insert(entity);

        // 通过OpenFeign RPC调用stock-service扣库存
        // 若响应超时（>10s）或返回错误 → 自动触发SEATA全局事务回滚
        ApiResponse<Void> resp = stockFeignClient.deduct(
                new DeductStockRequest(request.getProductCode(), request.getQuantity()));
        if (resp == null || resp.code() != 0) {
            String msg = resp == null ? "stock-service no response" : resp.message();
            logger.warn("deduct stock failed, will rollback global tx: {}", msg);
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "deduct stock failed: " + msg);
        }

        // 订单创建成功，发送事件消息到RocketMQ
        OrderEvent event = new OrderEvent(
                "ORDER_CREATED",
                entity.getId(),
                entity.getUserId(),
                entity.getProductCode(),
                entity.getQuantity(),
                entity.getAmount(),
                entity.getCreatedAt()
        );
        orderEventProducer.sendOrderCreatedEvent(event);

        return toVO(entity);
    }

    /**
     * 查询用户的所有订单，按创建时间倒序排列
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    @Override
    public List<OrderVO> listByUser(Long userId) {
        List<OrderEntity> list = orderMapper.selectList(
                Wrappers.<OrderEntity>lambdaQuery()
                        .eq(OrderEntity::getUserId, userId)
                        .orderByDesc(OrderEntity::getCreatedAt));
        return list.stream().map(this::toVO).toList();
    }

    private OrderVO toVO(OrderEntity entity) {
        return new OrderVO(
                entity.getId(),
                entity.getUserId(),
                entity.getProductCode(),
                entity.getQuantity(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt());
    }
}
