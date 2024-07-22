package com.genspark.product_service.repositories;

import com.genspark.product_service.entities.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends MongoRepository<Promotion, String> {

}
