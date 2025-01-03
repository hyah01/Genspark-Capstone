package com.genspark.cart_service.repository;

import com.genspark.cart_service.model.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends MongoRepository<WishList, String> {

    @Query("{ 'cartId' : ?0 }") // Get all data with matching cartId
    List<WishList> getWishListByCartId(String cartId);

    @Query(value = "{ 'cartId' : ?0 }", delete = true) // Delete all data with matching cartId
    void deleteAllByCartId(String cartId);
}
