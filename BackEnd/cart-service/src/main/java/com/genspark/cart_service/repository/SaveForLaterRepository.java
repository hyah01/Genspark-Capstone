package com.genspark.cart_service.repository;

import com.genspark.cart_service.model.SaveForLaterItems;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaveForLaterRepository extends MongoRepository<SaveForLaterItems, String> {

    @Query("{ 'cartId' : ?0 }") // Get all data with matching cartId
    List<SaveForLaterItems> getSFLByCartId(String cartId);

    @Query(value = "{ 'cartId' : ?0 }", delete = true) // Delete all data with matching cartId
    void deleteAllByCartId(String cartId);
}
