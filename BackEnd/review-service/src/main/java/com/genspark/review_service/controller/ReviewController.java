package com.genspark.review_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {
    @Autowired
    BookService service;

    @GetMapping("/review")
    public List<Book> findallReviews() {
        return service.findAllReviews();
    }
    // @GetMappingk
}
