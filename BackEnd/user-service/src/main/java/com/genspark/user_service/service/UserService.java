package com.genspark.user_service.service;

import com.genspark.user_service.entity.User;
import com.genspark.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    public User findUserByIdService(String id) {
        return repository.findUserById(id);
    }
}
