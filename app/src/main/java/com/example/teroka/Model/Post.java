package com.example.teroka.Model;

public class Post {
    private String date, place, postid, publisher, review, reviewby, reviewimage;

    public Post() {

    }

    public Post(String date, String place, String postid, String publisher, String review, String reviewby, String reviewimage) {
        this.date = date;
        this.place = place;
        this.postid = postid;
        this.publisher = publisher;
        this.review = review;
        this.reviewby = reviewby;
        this.reviewimage = reviewimage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewby() {
        return reviewby;
    }

    public void setReviewby(String reviewby) {
        this.reviewby = reviewby;
    }

    public String getReviewimage() {
        return reviewimage;
    }

    public void setReviewimage(String reviewimage) {
        this.reviewimage = reviewimage;
    }
}
