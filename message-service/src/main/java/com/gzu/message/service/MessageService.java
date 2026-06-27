package com.gzu.message.service;

import com.gzu.message.vo.MessageEventVO;

import java.util.List;
import java.util.Map;

public interface MessageService {
    void publish(Map<String, Object> payload);

    List<MessageEventVO> list();
}

