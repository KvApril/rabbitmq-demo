package com.kv.mqdemo.service;

import com.kv.mqdemo.dao.UserMapper;
import com.kv.mqdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User queryUserById(Integer id) {
        return userMapper.queryUser(id);
    }
}
