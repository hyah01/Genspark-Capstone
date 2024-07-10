package com.genspark.review_service.Entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Review {
    @Id
    long reviewId;
    long userId;
    long productId;
    String comment;
    int rating;
    Date date;

    public Review(long reviewId, long userId, long productId, String comment, int rating, Date date) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }

}
