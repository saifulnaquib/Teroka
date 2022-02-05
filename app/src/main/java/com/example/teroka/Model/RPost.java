package com.example.teroka.Model;

public class RPost {
    private String date, place, postid, publisher, recommendation, recommendby, recommendimage;

    public RPost() {

    }

    public RPost(String date, String place, String postid, String publisher, String recommendation, String recommendby, String recommendimage) {
        this.date = date;
        this.place = place;
        this.postid = postid;
        this.publisher = publisher;
        this.recommendation = recommendation;
        this.recommendby = recommendby;
        this.recommendimage = recommendimage;
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

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getRecommendby() {
        return recommendby;
    }

    public void setRecommendby(String recommendby) {
        this.recommendby = recommendby;
    }

    public String getRecommendimage() {
        return recommendimage;
    }

    public void setRecommendimage(String recommendimage) {
        this.recommendimage = recommendimage;
    }
}
