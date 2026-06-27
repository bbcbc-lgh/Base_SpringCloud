package com.gzu.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzu.common.core.exception.BusinessException;
import com.gzu.common.core.exception.ErrorCode;
import com.gzu.common.core.util.PasswordUtil;
import com.gzu.user.dto.CreateUserRequest;
import com.gzu.user.entity.UserEntity;
import com.gzu.user.mapper.UserMapper;
import com.gzu.user.service.UserService;
import com.gzu.user.vo.UserInfoVO;
import com.gzu.user.vo.UserProfileVO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserInfoVO create(CreateUserRequest request) {
        Long count = userMapper.selectCount(
                Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, request.getUsername()));
        if (count != null && count > 0) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "username already exists");
        }

        String role = request.getRole() == null || request.getRole().isBlank() ? "USER" : request.getRole();
        UserEntity entity = new UserEntity();
        entity.setUsername(request.getUsername());
        entity.setPassword(PasswordUtil.hashPassword(request.getPassword()));
        entity.setRole(role);
        userMapper.insert(entity);
        return new UserInfoVO(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());
    }

    @Override
    public UserInfoVO findByUsername(String username) {
        UserEntity user = userMapper.selectOne(
                Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, username).last("limit 1"));
        if (user == null) {
            return null;
        }
        return new UserInfoVO(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }

    @Override
    public UserProfileVO findProfileById(Long id) {
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "user not found");
        }
        return new UserProfileVO(user.getId(), user.getUsername(), user.getRole());
    }
}
