package com.gzu.auth.service.impl;

import com.gzu.auth.dto.LoginRequest;
import com.gzu.auth.dto.RegisterRequest;
import com.gzu.auth.feign.UserFeignClient;
import com.gzu.auth.service.AuthService;
import com.gzu.auth.vo.TokenVO;
import com.gzu.auth.vo.UserInfoVO;
import com.gzu.common.core.exception.BusinessException;
import com.gzu.common.core.exception.ErrorCode;
import com.gzu.common.core.result.ApiResponse;
import com.gzu.common.core.security.TokenUtil;
import com.gzu.common.redis.utils.RedisCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gzu.common.core.util.PasswordUtil;
import java.time.Duration;
import java.time.Instant;

/**
 * 认证服务实现类
 * 职责：处理用户登录、注册和Token生成，集成用户服务进行用户验证
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final UserFeignClient userFeignClient;
    private final RedisCacheService redisCacheService;

    @Value("${app.security.secret:gzu-demo-secret}")
    private String secret;

    @Value("${app.security.ttl-minutes:120}")
    private long ttlMinutes;

    public AuthServiceImpl(UserFeignClient userFeignClient, RedisCacheService redisCacheService) {
        this.userFeignClient = userFeignClient;
        this.redisCacheService = redisCacheService;
    }

    /**
     * 用户登录流程
     * 1. 调用user-service通过username查询用户信息（OpenFeign远程调用）
     * 2. 验证用户存在性，若用户不存在抛异常
     * 3. 验证密码正确性（使用加盐hash算法）
     * 4. 密码验证通过后生成JWT token并缓存到Redis
     * @param request 登录请求(username/password)
     * @return TokenVO包含token字符串和过期时间戳(毫秒)
     * @throws BusinessException 用户不存在或密码错误时抛出401异常，服务不可用时捕获Fallback
     */
    @Override
    public TokenVO login(LoginRequest request) {
        // 通过OpenFeign调用user-service查询用户信息
        // 若user-service不可用，会触发UserFeignClientFallback返回SERVICE_UNAVAILABLE
        ApiResponse<UserInfoVO> response = userFeignClient.findByUsername(request.getUsername());
        UserInfoVO user = response.data();
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "username or password is invalid");
        }

        // 使用PasswordUtil.verifyPassword进行密码验证（防止彩虹表攻击）
        if (!PasswordUtil.verifyPassword(request.getPassword(), user.password())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "username or password is invalid");
        }
        return buildToken(user);
    }

    /**
     * 用户注册流程
     * 1. 调用user-service创建新用户（OpenFeign远程调用）
     * 2. 创建成功后立即签发Token并返回
     * @param request 注册请求(username/password/role)，role默认为USER
     * @return TokenVO包含token字符串和过期时间戳(毫秒)
     * @throws BusinessException 用户已存在或创建失败时抛异常，服务不可用时捕获Fallback
     */
    @Override
    public TokenVO register(RegisterRequest request) {
        // 通过OpenFeign调用user-service创建用户
        // 若user-service不可用，会触发UserFeignClientFallback返回SERVICE_UNAVAILABLE
        ApiResponse<UserInfoVO> response = userFeignClient.create(request);
        if (response == null) {
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "user service temporarily unavailable");
        }
        if (response.code() != ErrorCode.SUCCESS.getCode()) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, response.message());
        }
        UserInfoVO user = response.data();
        if (user == null) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "create user failed");
        }
        return buildToken(user);
    }

    /**
     * Token生成和缓存
     * 1. 使用配置的TTL(默认120分钟)和secret生成JWT token
     * 2. Token格式: base64(payload)|.hmac_signature
     * 3. 将token缓存到Redis，key为"token:user:{userId}"，过期时间等于token TTL
     * 4. 返回TokenVO，包含token字符串和过期时间戳(毫秒级)
     * @param user 用户信息对象(id/username/role)
     * @return TokenVO包含JWT token和过期时间戳
     */
    private TokenVO buildToken(UserInfoVO user) {
        Duration ttl = Duration.ofMinutes(ttlMinutes);
        // TokenUtil.createToken使用HmacSHA256算法签名，确保token无法伪造
        String token = TokenUtil.createToken(user.id(), user.username(), user.role(), secret, ttl);
        long expiresAt = Instant.now().plus(ttl).toEpochMilli();
        // 缓存token到Redis，用于快速验证和Token撤销
        redisCacheService.set("token:user:" + user.id(), token, ttl);
        return new TokenVO(token, expiresAt);
    }
}
