package com.gzu.message.vo;

import java.time.LocalDateTime;
import java.util.Map;

public record MessageEventVO(Long id, String eventType, Map<String, Object> payload, LocalDateTime createdAt) {
}

