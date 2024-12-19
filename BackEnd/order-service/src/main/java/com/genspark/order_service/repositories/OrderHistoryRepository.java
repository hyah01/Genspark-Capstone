package com.genspark.order_service.repositories;

import com.genspark.order_service.entities.OrderHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends MongoRepository<OrderHistory, String> {
    List<OrderHistory> findAllByUserId(String userId);
}
