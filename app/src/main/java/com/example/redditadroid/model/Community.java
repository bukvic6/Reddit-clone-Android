package com.example.redditadroid.model;

public class Community {

    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String description;

    public String getUserId() {
        return userId;
    }

    String userId;

    public Community(String name, String description, String userId) {
        this.name = name;
        this.description = description;
        this.userId = userId;
    }
    public Community(String id,String name, String description, String userId) {
        this.id = id;
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
