package com.genspark.cart_service.repository;

import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.model.CartItems;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepository extends MongoRepository<CartItems, String> {

    @Query("{ 'cartId' : ?0 }") // Get all data with matching cartId
    List<CartItem> getAllCartOrderByCartId(String cartId);

    @Query(value = "{ 'cartId' : ?0 }", delete = true) // Delete all data with matching cartId
    void deleteAllByCartId(String cartId);
}
