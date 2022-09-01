package com.example.redditadroid.model;

import com.example.redditadroid.CommunityActivity;

public class  Post {
    String id;
    String title;
    String text;
    String reaction;
    String communityId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public Post(String id,String title, String text, String userId, String communityId, String reaction) {
        this.title = title;
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.reaction = reaction;
        this.communityId = communityId;
    }
    public Post(String id,String title, String text, String userId, String communityId) {
        this.title = title;
        this.id = id;
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
