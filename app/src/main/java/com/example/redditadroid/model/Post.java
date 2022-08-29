package com.example.redditadroid.model;

import com.example.redditadroid.CommunityActivity;

public class  Post {
    String title;
    String text;
    String communityId;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserId() {
        return userId;
    }

    String userId;

    public Post(String title, String text, String userId) {
        this.title = title;
        this.text = text;
        this.userId = userId;
    }
    public Post(String title, String text, String userId, String communityId) {
        this.title = title;
        this.text = text;
        this.userId = userId;
        this.communityId = communityId;
    }
    public Post(){}

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
