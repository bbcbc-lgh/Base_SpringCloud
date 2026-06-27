package com.gzu.auth.feign.fallback;

import com.gzu.auth.dto.RegisterRequest;
import com.gzu.auth.vo.UserInfoVO;
import com.gzu.common.core.exception.ErrorCode;
import com.gzu.common.core.result.ApiResponse;
import com.gzu.auth.feign.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientFallback implements UserFeignClient {
    private static final Logger logger = LoggerFactory.getLogger(UserFeignClientFallback.class);

    @Override
    public ApiResponse<UserInfoVO> findByUsername(String username) {
        logger.warn("user-service fallback triggered for findByUsername: {}", username);
        return ApiResponse.fail(ErrorCode.SERVICE_UNAVAILABLE, "user service temporarily unavailable");
    }

    @Override
    public ApiResponse<UserInfoVO> create(RegisterRequest request) {
        logger.warn("user-service fallback triggered for create user: {}", request.getUsername());
        return ApiResponse.fail(ErrorCode.SERVICE_UNAVAILABLE, "user service temporarily unavailable");
    }
}
