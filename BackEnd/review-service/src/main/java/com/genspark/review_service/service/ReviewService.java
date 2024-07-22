package com.genspark.review_service.service;

import com.genspark.review_service.entity.Review;
import com.genspark.review_service.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

//    public list<Book> findallReviews() {
//        return reviewRepository.findAll();
//    }

    public Review findReviewById(String id) {
        return reviewRepository.findReviewById(id);

        }
    }


