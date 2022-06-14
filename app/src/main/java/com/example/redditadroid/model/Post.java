package com.example.redditadroid.model;

public class  Post {
    String title, text;

    public Post(String title, String text) {
        this.title = title;
        this.text = text;
    }
    public Post(){}

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
