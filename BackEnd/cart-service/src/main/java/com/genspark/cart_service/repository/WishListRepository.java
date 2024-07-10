package com.genspark.cart_service.repository;

import com.genspark.cart_service.model.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishListRepository extends MongoRepository<WishList, String> {
}
