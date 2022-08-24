package com.example.redditadroid.model;

public class Community {
    String name;
    String description;

    public String getUserId() {
        return userId;
    }

    String userId;

    public Community(String name, String text, String userId) {
        this.name = name;
        this.description = description;
        this.userId = userId;
    }
    public Community(){}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
