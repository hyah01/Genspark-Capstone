package com.genspark.cart_service.repository;

import com.genspark.cart_service.model.CartOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CartOrderRepository extends MongoRepository<CartOrder, String> {

    @Query("{ 'cartId' : ?0 }")
    List<CartOrder> getAllCartOrderByCartId(String cartId);
}
