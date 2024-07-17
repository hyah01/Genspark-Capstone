package com.genspark.cart_service.repository;

import com.genspark.cart_service.model.CartOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartOrderRepository extends MongoRepository<CartOrder, String> {

    @Query("{ 'cartId' : ?0 }")
    List<CartOrder> getAllCartOrderByCartId(String cartId);

    @Query(value = "{ 'cartId' : ?0 }", delete = true)
    void deleteAllByCartId(String cartId);
}
