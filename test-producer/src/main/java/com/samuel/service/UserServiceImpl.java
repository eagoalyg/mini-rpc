package com.samuel.service;

import com.samuel.annotation.RpcService;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.stereotype.Service;


@Service
@RpcService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        user.setName("被调用方法");
        return user;
    }
}
