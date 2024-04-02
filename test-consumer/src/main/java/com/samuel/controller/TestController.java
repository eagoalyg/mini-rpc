package com.samuel.controller;

import com.samuel.annotation.RpcClient;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RpcClient(serviceName = "user")
    private UserService userService;

    @GetMapping("/test")
    public String test() {
        User user = new User();
        user.setName("客户段");
        user = userService.getUser(user);
        return user.getName();
    }
}
