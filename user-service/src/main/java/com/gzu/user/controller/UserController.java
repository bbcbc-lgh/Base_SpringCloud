package com.gzu.user.controller;

import com.gzu.common.core.exception.BusinessException;
import com.gzu.common.core.exception.ErrorCode;
import com.gzu.common.core.result.ApiResponse;
import com.gzu.common.core.security.TokenPayload;
import com.gzu.common.security.context.AuthContextHolder;
import com.gzu.user.service.UserService;
import com.gzu.user.vo.UserProfileVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileVO> me() {
        TokenPayload currentUser = AuthContextHolder.get();
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "missing user identity");
        }
        return ApiResponse.ok(userService.findProfileById(currentUser.userId()));
    }
}
