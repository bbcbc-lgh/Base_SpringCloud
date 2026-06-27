package com.gzu.auth.service;

import com.gzu.auth.dto.LoginRequest;
import com.gzu.auth.dto.RegisterRequest;
import com.gzu.auth.vo.TokenVO;

public interface AuthService {
    TokenVO login(LoginRequest request);

    TokenVO register(RegisterRequest request);
}

