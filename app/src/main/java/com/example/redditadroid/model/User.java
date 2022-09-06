package com.example.redditadroid.model;

public class User {
    public String role;
    public String username;
    public String email;
    public String password;
    public String displayName;
    public String profileDesc;

 public User(){}

    public User(String username, String email, String password, String displayName, String profileDesc) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.profileDesc = profileDesc;
    }
    public User(String username, String email, String password, String displayName, String profileDesc,String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.profileDesc = profileDesc;
        this.role = role;
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public void setProfileDesc(String profileDesc) {
        this.profileDesc = profileDesc;
    }

    public User(String textUsername, String textEmail) {
        this.username = textUsername;
        this.email = textEmail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
