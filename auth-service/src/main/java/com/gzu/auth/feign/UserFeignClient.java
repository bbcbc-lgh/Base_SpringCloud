package com.gzu.auth.feign;

import com.gzu.auth.dto.RegisterRequest;
import com.gzu.auth.vo.UserInfoVO;
import com.gzu.common.core.result.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/internal/users", fallback = com.gzu.auth.feign.fallback.UserFeignClientFallback.class)
public interface UserFeignClient {
    @GetMapping("/username/{username}")
    ApiResponse<UserInfoVO> findByUsername(@PathVariable("username") String username);

    @PostMapping
    ApiResponse<UserInfoVO> create(@RequestBody RegisterRequest request);
}

