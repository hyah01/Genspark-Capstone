package com.genspark.review_service.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    String reviewId;
    long userId;
    long productId;
    String comment;
    int rating;
    Date date;

    // public Review(long reviewId, long userId, long productId, String comment, int
    // rating, Date date) {
    // this.reviewId = reviewId;
    // this.userId = userId;
    // this.productId = productId;
    // this.comment = comment;
    // this.rating = rating;
    // this.date = date;
    // }

}
