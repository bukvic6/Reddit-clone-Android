package com.example.redditadroid.model;

import com.example.redditadroid.CommunityActivity;

import java.time.LocalDate;

public class  Post {
    String id;
    String title;
    String text;
    String reaction;
    String communityId;
    String creationDate;


    public Post(String id, String title, String text, String user, String communityId, String reaction, String creationDate) {
        this.title = title;
        this.id = id;
        this.text = text;
        this.userId = user;
        this.reaction = reaction;
        this.communityId = communityId;
        this.creationDate = creationDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

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



    public Post(){}

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
