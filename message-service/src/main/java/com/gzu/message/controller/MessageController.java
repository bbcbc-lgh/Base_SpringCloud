package com.gzu.message.controller;

import com.gzu.common.core.result.ApiResponse;
import com.gzu.message.service.MessageService;
import com.gzu.message.vo.MessageEventVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/events")
    public ApiResponse<List<MessageEventVO>> list() {
        return ApiResponse.ok(messageService.list());
    }
}

