package com.genspark.cart_service.repository;

import com.genspark.cart_service.model.CartOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaveForLaterRepository extends MongoRepository<CartOrder, String> {
}
