package com.example.redditadroid.model;

public class Comment {
    String id;
    String userId;
    String text;
    String postId;
    String creationDate;
    String reaction;
    public Comment(){

    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public Comment(String id, String userId, String text, String postId, String creationDate, String reaction) {
        this.userId = userId;
        this.id = id;
        this.text = text;
        this.postId = postId;
        this.reaction = reaction;
        this.creationDate = creationDate;
    }

    public Comment(String id, String userId, String text, String creationDate) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
