package com.example.bc_quanlibanhangonline.models;

public class Review {
    private int reviewId;
    private int userId;
    private int productId;
    private int rating;
    private String comment;
    private String reviewDate;

    public Review(int reviewId, int userId, int productId,
                  int rating, String comment, String reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }
}
