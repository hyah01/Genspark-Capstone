package com.genspark.user_service.controller;

import com.genspark.user_service.entity.User;
import com.genspark.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService service;

    @GetMapping("/user")
    public User findUser(@PathVariable String id) {
        return service.findUserByIdService(id);
    }
    // @GetMappingk
}