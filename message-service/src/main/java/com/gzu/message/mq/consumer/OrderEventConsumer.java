package com.gzu.message.mq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzu.message.config.RocketMQConfig;
import com.gzu.message.service.MessageService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RocketMQMessageListener(
        topic = RocketMQConfig.ORDER_TOPIC,
        consumerGroup = "${rocketmq.consumer.group:message-service-consumer-group}",
        selectorExpression = RocketMQConfig.ORDER_CREATED_TAG)
public class OrderEventConsumer implements RocketMQListener<String> {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);
    
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    public OrderEventConsumer(MessageService messageService, ObjectMapper objectMapper) {
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(String body) {
        try {
            logger.info("Received order event: {}", body);
            
            // 将 JSON 转换为 Map
            @SuppressWarnings("unchecked")
            Map<String, Object> payload = objectMapper.readValue(body, Map.class);
            
            // 发布到消息服务
            messageService.publish(payload);
            logger.info("Order event processed successfully");
            
        } catch (Exception e) {
            logger.error("Failed to process order event", e);
            throw new IllegalStateException("process order event failed", e);
        }
    }
}
