package com.genspark.user_service.repository;

import com.genspark.user_service.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{id: '?0'}")
    User findUserById(String id);
}