package com.genspark.order_service.repositories;

import com.genspark.order_service.entities.OrderHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends MongoRepository<OrderHistory, String> {
}
