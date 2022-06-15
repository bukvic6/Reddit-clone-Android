package com.example.redditadroid.model;

public class  Post {
    String title;
    String text;

    public String getUserId() {
        return userId;
    }

    String userId;

    public Post(String title, String text, String userId) {
        this.title = title;
        this.text = text;
        this.userId = userId;
    }
    public Post(){}

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
