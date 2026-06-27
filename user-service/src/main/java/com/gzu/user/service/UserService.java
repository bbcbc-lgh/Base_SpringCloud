package com.gzu.user.service;

import com.gzu.user.dto.CreateUserRequest;
import com.gzu.user.vo.UserInfoVO;
import com.gzu.user.vo.UserProfileVO;

public interface UserService {
    UserInfoVO create(CreateUserRequest request);

    UserInfoVO findByUsername(String username);

    UserProfileVO findProfileById(Long id);
}

