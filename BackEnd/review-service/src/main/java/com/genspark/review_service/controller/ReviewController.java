package com.genspark.review_service.controller;

import com.genspark.review_service.entity.Review;
import com.genspark.review_service.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {
    @Autowired
    ReviewService service;

//    @GetMapping("/review")
//    public List<Review> findallReviews() {
//        return service.findAllReviews();
//    }
     @GetMapping("/review/{id}")
    public Review findReview(@PathVariable String id) {
         return service.findReviewById(id);
     }
}
