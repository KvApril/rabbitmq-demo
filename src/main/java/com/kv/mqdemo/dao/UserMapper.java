package com.kv.mqdemo.dao;

import com.kv.mqdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User queryUser(Integer id);
}
