package com.example.redditadroid.model;

public class Comment {
    String userId;
    String text;
    String postId;
    String creationDate;
    public Comment(){

    }

    public Comment(String userId, String text, String postId, String creationDate) {
        this.userId = userId;
        this.text = text;
        this.postId = postId;
        this.creationDate = creationDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
