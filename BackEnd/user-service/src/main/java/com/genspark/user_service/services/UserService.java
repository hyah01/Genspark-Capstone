package com.genspark.user_service.services;

import com.genspark.user_service.entities.User;

import java.util.List;

public interface UserService {
    boolean emailExists(String email);

    boolean isValid(String password);

}
