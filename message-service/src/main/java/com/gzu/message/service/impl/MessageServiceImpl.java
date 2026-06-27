package com.gzu.message.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzu.message.entity.MessageEvent;
import com.gzu.message.mapper.MessageEventMapper;
import com.gzu.message.service.MessageService;
import com.gzu.message.vo.MessageEventVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageEventMapper messageEventMapper;
    private final ObjectMapper objectMapper;

    public MessageServiceImpl(MessageEventMapper messageEventMapper, ObjectMapper objectMapper) {
        this.messageEventMapper = messageEventMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(Map<String, Object> payload) {
        String eventType = String.valueOf(payload.getOrDefault("eventType", "UNKNOWN"));
        MessageEvent entity = new MessageEvent();
        entity.setEventType(eventType);
        entity.setPayload(serialize(payload));
        entity.setCreatedAt(LocalDateTime.now());
        messageEventMapper.insert(entity);
    }

    @Override
    public List<MessageEventVO> list() {
        List<MessageEvent> events = messageEventMapper.selectList(
                Wrappers.<MessageEvent>lambdaQuery().orderByDesc(MessageEvent::getCreatedAt));
        return events.stream()
                .map(e -> new MessageEventVO(e.getId(), e.getEventType(), deserialize(e.getPayload()), e.getCreatedAt()))
                .toList();
    }

    private String serialize(Map<String, Object> payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("serialize message payload failed", ex);
        }
    }

    private Map<String, Object> deserialize(String payload) {
        try {
            return objectMapper.readValue(payload, new TypeReference<>() {
            });
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("deserialize message payload failed", ex);
        }
    }
}
