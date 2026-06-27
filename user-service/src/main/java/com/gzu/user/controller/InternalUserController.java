package com.gzu.user.controller;

import com.gzu.common.core.result.ApiResponse;
import com.gzu.user.dto.CreateUserRequest;
import com.gzu.user.service.UserService;
import com.gzu.user.vo.UserInfoVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {
    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/username/{username}")
    public ApiResponse<UserInfoVO> findByUsername(@PathVariable("username") String username) {
        return ApiResponse.ok(userService.findByUsername(username));
    }

    @PostMapping
    public ApiResponse<UserInfoVO> create(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.ok(userService.create(request));
    }
}

