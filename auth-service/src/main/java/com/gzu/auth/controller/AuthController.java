package com.gzu.auth.controller;

import com.gzu.auth.dto.LoginRequest;
import com.gzu.auth.dto.RegisterRequest;
import com.gzu.auth.service.AuthService;
import com.gzu.auth.vo.TokenVO;
import com.gzu.common.core.result.ApiResponse;
import com.gzu.common.core.security.TokenPayload;
import com.gzu.common.security.context.AuthContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 认证服务控制器，处理用户登录、注册和获取当前用户信息
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 用户登录接口：验证用户名和密码，返回JWT令牌和过期时间
    // 参数验证: username和password不能为空
    // 返回: TokenVO包含token字符串和过期时间戳(毫秒)
    // 异常: 用户不存在或密码错误时返回401 UNAUTHORIZED
    @PostMapping("/login")
    public ApiResponse<TokenVO> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    // 用户注册接口：创建新用户并生成令牌
    // 参数验证: username和password不能为空，role默认为USER
    // 返回: TokenVO包含token字符串和过期时间戳(毫秒)
    // 异常: 用户已存在时返回业务错误
    @PostMapping("/register")
    public ApiResponse<TokenVO> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok(authService.register(request));
    }

    // 获取当前认证用户信息接口
    // 从ThreadLocal上下文中提取当前请求的用户身份(需要有效的Authorization header)
    // 返回: TokenPayload包含userId、username、role、issuedAt、expiresAt
    // 异常: 未认证或token无效时返回401 UNAUTHORIZED
    @GetMapping("/me")
    public ApiResponse<TokenPayload> me() {
        TokenPayload currentUser = AuthContextHolder.get();
        return ApiResponse.ok(currentUser);
    }
}
