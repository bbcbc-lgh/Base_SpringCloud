package com.gzu.message.controller;

import com.gzu.common.core.result.ApiResponse;
import com.gzu.message.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/internal/messages")
public class InternalMessageController {
    private final MessageService messageService;

    public InternalMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/events")
    public ApiResponse<Void> publish(@RequestBody Map<String, Object> payload) {
        messageService.publish(payload);
        return ApiResponse.ok(null);
    }
}

