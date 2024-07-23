package com.genspark.review_service.repository;

import com.genspark.review_service.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    @Query("{id: '?0'}")
    Review findReviewById(String id);
}