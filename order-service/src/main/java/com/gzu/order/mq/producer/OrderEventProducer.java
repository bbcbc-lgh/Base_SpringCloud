package com.gzu.order.mq.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzu.order.config.RocketMQConfig;
import com.gzu.order.mq.event.OrderEvent;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderEventProducer.class);
    
    private final RocketMQTemplate rocketMQTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventProducer(RocketMQTemplate rocketMQTemplate, ObjectMapper objectMapper) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrderCreatedEvent(OrderEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            rocketMQTemplate.syncSend(RocketMQConfig.ORDER_CREATED_DESTINATION, message);
            logger.info("Order event sent: orderId={}, eventType={}", event.getOrderId(), event.getEventType());
        } catch (Exception e) {
            logger.error("Failed to send order event: orderId={}", event.getOrderId(), e);
            throw new RuntimeException("Failed to send order event", e);
        }
    }
}
