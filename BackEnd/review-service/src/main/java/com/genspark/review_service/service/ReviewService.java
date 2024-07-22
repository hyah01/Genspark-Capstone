package com.genspark.review_service.service;

import com.genspark.review_service.Entity.Review;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public list<Book> findallReviews() {
        return reviewRepository.findAll();
    }

    public Book findReviewById(Long id) {
        return repo.findReviewById() {

        }
    }

}
