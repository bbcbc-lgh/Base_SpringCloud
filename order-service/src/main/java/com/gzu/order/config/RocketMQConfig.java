package com.gzu.order.config;

public final class RocketMQConfig {

    public static final String ORDER_TOPIC = "order-topic";
    public static final String ORDER_CREATED_TAG = "ORDER_CREATED";
    public static final String ORDER_CREATED_DESTINATION = ORDER_TOPIC + ":" + ORDER_CREATED_TAG;

    private RocketMQConfig() {
    }
}
