package com.genspark.cart_service.repository;

import com.genspark.cart_service.model.CartOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveForLaterRepository extends MongoRepository<CartOrder, String> {
}
