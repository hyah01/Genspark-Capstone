package com.genspark.user_service.services;

import com.genspark.user_service.entities.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User getUserById(String id);
    List<User> getAllUsers();
    User updateUser(String id, User user);
    boolean deleteUser(String id);
    UserInfoDetails loadUserByUsername(String email);

}
