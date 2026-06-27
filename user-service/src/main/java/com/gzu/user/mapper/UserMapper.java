package com.gzu.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzu.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
