package com.genspark.review_service.model;

import java.util.Date;

public class Review {
    private long reviewId;
    private long userId;
    private long productId;
    private String comment;
    private int rating;
    private Date date;

    public Review(long reviewId, long userId, long productId, String comment, int rating, Date date) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
